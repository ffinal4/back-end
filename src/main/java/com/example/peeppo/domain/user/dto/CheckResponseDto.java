package com.example.peeppo.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckResponseDto {

    private String msg;

    private boolean checkValidate;

    private int statusCode;

    private String statusMessage;

}