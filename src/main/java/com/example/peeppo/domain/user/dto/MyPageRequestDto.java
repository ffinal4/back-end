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
public class MyPageRequestDto {

    @NotBlank
    @Size(min = 2, max = 15, message = "2자 이상 15자 이내로 입력해주세요.")
    @Pattern(regexp = "^[a-z0-9]+$", message = "소문자 영어와 숫자만 사용 가능합니다.")
    String nickname;

    @NotBlank
    @Pattern(regexp = "^(?=.*?[A-Za-z])(?=.*?[0-9])[A-Za-z\\d~!@#$%^&*()+|=]{8,}$")
    String password;

    @NotBlank
    String location;
}
