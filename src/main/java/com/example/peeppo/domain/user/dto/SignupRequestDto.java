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
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "영어와 숫자, 한글만 사용 가능합니다.")
    String nickname;

    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank
    @Size(min = 10, max = 30, message = "10자 이상 30자 이내로 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    String email;

    @NotBlank
    @Size(message = "알파벳 문자, 숫자, 특수문자 포함이 되어야 하며, 8자리 이상, 15자리 이하여야 합니다")
    @Pattern(regexp = "^(?=.*?[A-Za-z])(?=.*?[0-9])[A-Za-z\\d~!@#$%^&*()+|=]{8,15}$")
    String password;

    @NotBlank
    String location;
}
