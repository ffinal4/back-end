package com.example.peeppo.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRequestDto {

    @Size(message = "알파벳 문자, 숫자, 특수문자 포함이 되어야 하며, 8자리 이상, 15자리 이하여야 합니다")
    @Pattern(regexp = "^(?=.*?[A-Za-z])(?=.*?[0-9])[A-Za-z\\d~!@#$%^&*()+|=]{8,15}$")
    String password;

    @NotBlank
    String originPassword;
}
