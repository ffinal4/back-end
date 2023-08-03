package com.example.peeppo.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailCheckResponseDto {

    private String msg;

    private boolean checkValidate;

    private int statusCode;

    private String statusMessage;

}