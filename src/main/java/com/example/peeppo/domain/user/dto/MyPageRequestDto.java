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
public class MyPageRequestDto {

    @NotBlank
    @Size(min = 2, max = 15, message = "2자 이상 15자 이내로 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "영어와 숫자, 한글만 사용 가능합니다.")
    String nickname;

    @NotBlank
    String location;
}
