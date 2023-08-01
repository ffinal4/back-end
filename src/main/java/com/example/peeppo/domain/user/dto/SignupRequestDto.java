package com.example.peeppo.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.PROTECTED;

@Getter
@AllArgsConstructor(access = PROTECTED)
public class SignupRequestDto {

    @NotBlank
    String nickname;

    @NotBlank
    String name;

    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일 형식이 아닙니다.")
    String email;

    //패턴 상의해둘것
    @NotBlank
    String password;
}
