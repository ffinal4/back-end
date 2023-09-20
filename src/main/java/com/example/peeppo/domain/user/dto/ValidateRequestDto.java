package com.example.peeppo.domain.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValidateRequestDto {

    @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", flags = Pattern.Flag.CASE_INSENSITIVE)
    String nickname;
}
