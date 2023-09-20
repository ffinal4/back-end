package com.example.peeppo.global.exception;


import com.example.peeppo.global.stringCode.ErrorCodeEnum;

public class InvalidConditionException extends IllegalArgumentException{

    ErrorCodeEnum errorCodeEnum;

    public InvalidConditionException(ErrorCodeEnum errorCodeEnum) {
        this.errorCodeEnum = errorCodeEnum;
    }
}