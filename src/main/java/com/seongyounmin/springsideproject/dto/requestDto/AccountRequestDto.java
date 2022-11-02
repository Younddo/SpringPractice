package com.seongyounmin.springsideproject.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDto {


    @NotBlank(message = "아이디는 공백일 수 없습니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotBlank(message = "이름은 공백일 수 없습니다.")
    private String accountName;

    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    @Size(min = 8, max = 16, message = "비밀번호는 8~16 개의 문자만 허용합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$"
            , message = "비밀번호는 무조건 영문, 숫자, 특수문자를 각각 1글자 이상 포함해야 합니다.")
    private String accountPw;

    @NotBlank(message = "비밀번호확인은 공백일 수 없습니다.")
    private String accountPwConfirm;

    @NotBlank(message = "핸드폰 번호는 공백일 수 없습니다.")
    private String phoneNum;

    public void setEncodePwd(String encodePwd) {

        this.accountPw = encodePwd;
    }

}
