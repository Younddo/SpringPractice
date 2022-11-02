package com.seongyounmin.springsideproject.controller;

import com.seongyounmin.springsideproject.config.UserDetailsImpl;
import com.seongyounmin.springsideproject.dto.requestDto.AccountRequestDto;
import com.seongyounmin.springsideproject.global.GlobalResponseDto;
import com.seongyounmin.springsideproject.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AccountController {

    private final AccountService accountService;
    //회원가입
    @PostMapping("/auth/signup")
    public GlobalResponseDto signup(@Valid @RequestBody AccountRequestDto accountRequestDto) {
        return accountService.signup(accountRequestDto);
    }

    // 아이디 중복확인
    @PostMapping("/auth/idCheck")
    public GlobalResponseDto idCheck(@Valid @RequestBody EmailRequestDto emailRequestDto) {
        return accountService.idCheck(emailRequestDto);
    }

    // 닉네임 중복확인
    @PostMapping("/auth/nameCheck")
    public GlobalResponseDto nameCheck(@Valid @RequestBody AccountNameRequestDto accountNameRequestDto) {
        return accountService.nameCheck(accountNameRequestDto);
    }

    //로그인
    @PostMapping("/auth/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return accountService.login(loginRequestDto, response);
    }


    //로그아웃
    @DeleteMapping("/api/logout")
    public GlobalResponseDto logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return accountService.logout(userDetails);
    }

}
