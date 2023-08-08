/*
package com.example.peeppo.global.exception;

import com.example.peeppo.global.exception.InvalidConditionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@ResponseBody
@Slf4j
public class PeeppoExceptionHandler {


//    @ExceptionHandler(IllegalArgumentException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity handleIllegalArgumentException(IllegalArgumentException e){
//        return ResponseEntity.badRequest().body(e.getMessage());
//    }

    @ExceptionHandler(InvalidConditionException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiResponse<?> handleInvalidConditionException(InvalidConditionException e) {
        return ResponseUtils.customError(e.errorCodeEnum);
    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiResponse<?> handleRuntimeException(RuntimeException e) {
        log.error("error message = {}", e.getMessage());
        return ResponseUtils.error(e.getMessage(), BAD_REQUEST.value());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiResponse<?> handlerException(Exception e) {
        log.error("error message = {}", e.getMessage());
        return ResponseUtils.error(e.getMessage(), BAD_REQUEST.value());
    }

}
*/