package com.example.peeppo.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    @Email
    @NotBlank
    String email;

    @NotBlank
    String password;
}
