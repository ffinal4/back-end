package com.example.peeppo.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    @NotBlank
    @Size(min = 2, max = 15, message = "2자 이상 15자 이내로 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "영문자와 한글, 숫자만 사용 가능합니다.")
    String nickname;

    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank
    @Size(min = 2, max = 30, message = "2자 이상 15자 이내로 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    String email;

    @NotBlank
    @Size(min = 8, max = 15)
    @Pattern(regexp = "^(?=.*?[A-Za-z])(?=.*?[0-9])[A-Za-z\\d~!@#$%^&*()+|=]{8,}$")
    String password;

    @NotBlank
    String location;
}
