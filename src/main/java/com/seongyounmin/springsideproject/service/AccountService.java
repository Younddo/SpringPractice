package com.seongyounmin.springsideproject.service;

import com.seongyounmin.springsideproject.config.UserDetailsImpl;
import com.seongyounmin.springsideproject.dto.requestDto.AccountRequestDto;
import com.seongyounmin.springsideproject.entity.Account;
import com.seongyounmin.springsideproject.exception.CustomException;
import com.seongyounmin.springsideproject.exception.ErrorCode;
import com.seongyounmin.springsideproject.global.GlobalResponseDto;
import com.seongyounmin.springsideproject.jwt.JwtUtil;
import com.seongyounmin.springsideproject.jwt.TokenDto;
import com.seongyounmin.springsideproject.repository.AccountRepository;
import com.seongyounmin.springsideproject.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    //회원가입
    @Transactional
    public GlobalResponseDto signup(AccountRequestDto accountRequestDto) {
        if(accountRepository.findByEmail(accountRequestDto.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.AlreadyHaveEmail);
        }
        if(accountRepository.findByAccountName(accountRequestDto.getAccountName()).isPresent()) {
            throw new CustomException(ErrorCode.AlreadyHaveName);
        }

        // 비밀번호 일치 확인
        String password = accountRequestDto.getAccountPw();
        if(!password.equals(accountRequestDto.getAccountPwConfirm())){
            throw new CustomException(ErrorCode.NotMatchPassword);
        }

        // 비밀번호 암호화
        accountRequestDto.setEncodePwd(passwordEncoder.encode(accountRequestDto.getAccountPw()));

        Account account = new Account(accountRequestDto);
        account.setMyPage(new MyPage(account));

        accountRepository.save(account);

        return new GlobalResponseDto("Success signup", HttpStatus.OK.value());
    }

    //아이디 중복확인
    @Transactional
    public GlobalResponseDto idCheck(EmailRequestDto emailRequestDto) {
        if(accountRepository.findByEmail(emailRequestDto.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.AlreadyHaveEmail);
        }
        return new GlobalResponseDto("Success email check", HttpStatus.OK.value());
    }

    //이메일 중복확인
    @Transactional
    public GlobalResponseDto nameCheck(AccountNameRequestDto accountNameRequestDto) {
        if(accountRepository.findByAccountName(accountNameRequestDto.getAccountName()).isPresent()) {
            throw new CustomException(ErrorCode.AlreadyHaveName);
        }
        return new GlobalResponseDto("Success account name check", HttpStatus.OK.value());
    }

    // 로그인
    public LoginResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        //아이디가 존재하는지 확인
        Account account = accountRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(
                () -> new CustomException(ErrorCode.NotFoundUser)
        );

        // 비밀번호 맞는지 확인
        if(!passwordEncoder.matches(loginRequestDto.getAccountPw(), account.getAccountPw())) {
            throw new CustomException(ErrorCode.NotMatchPassword);
        }

        //토큰 발급
        TokenDto tokenDto = jwtUtil.createAllToken(loginRequestDto.getEmail());

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(loginRequestDto.getEmail());

        // 로그아웃한 후 로그인을 다시 하는가?
        if(refreshToken.isPresent()) {
            RefreshToken refreshToken1 = refreshToken.get().updateToken(tokenDto.getRefreshToken());
            refreshTokenRepository.save(refreshToken1);
        } else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), loginRequestDto.getEmail());
            refreshTokenRepository.save(newToken);
        }

        //토큰을 header에 넣어서 클라이언트에게 전달하기
        setHeader(response, tokenDto);

        return new LoginResponseDto(account.getAccountName(), "Success Login", HttpStatus.OK.value());
    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }

    @Transactional
    public GlobalResponseDto logout(UserDetailsImpl userDetails) {
        refreshTokenRepository.deleteAllByAccountEmail(userDetails.getAccount().getEmail());
        return new GlobalResponseDto("Success Logout", HttpStatus.OK.value());
    }

}
