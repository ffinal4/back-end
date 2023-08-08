package com.example.peeppo.domain.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LogoutRequestDto {

    @NotNull(message = "잘못된 요청입니다.")
    private String accessToken;

    @NotNull(message = "잘못된 요청입니다.")
    private String refreshToken;
}
