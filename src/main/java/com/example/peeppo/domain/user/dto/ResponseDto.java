package com.example.peeppo.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto {

    private String msg;

    private int statusCode;

    private String statusMessage;

}