package com.example.peeppo.global.exception;

import com.example.peeppo.domain.user.dto.ResponseDto;
import com.example.peeppo.global.responseDto.ApiResponse;
import com.example.peeppo.global.responseDto.ErrorResponse;
import jdk.jshell.Snippet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "Global exception")
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ApiResponse<ErrorResponse> handleNullPointerException(Exception ex) {
        log.error("NullPointerException error: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        ApiResponse<ErrorResponse> apiResponse = new ApiResponse<>(false, null, errorResponse);
        return apiResponse;
    }

//    @ExceptionHandler(IllegalArgumentException.class)
//    public ApiResponse<ErrorResponse> handleIllegalArgumentException(Exception ex) {
//        log.error("IllegalArgumentException error: {}", ex.getMessage());
//        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
//        ApiResponse<ErrorResponse> apiResponse = new ApiResponse<>(false, null, errorResponse);
//        return apiResponse;
//    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDto> handleIllegalArgumentException(Exception ex) {
        log.error("IllegalArgumentException error: {}", ex.getMessage());
        ResponseDto responseDto = new ResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
        ResponseEntity<ResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        return response;
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ApiResponse<ErrorResponse> handleIllegalAccessException(Exception ex) {
        log.error("IllegalAccessException error: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        ApiResponse<ErrorResponse> apiResponse = new ApiResponse<>(false, null, errorResponse);
        return apiResponse;
    }
    @ExceptionHandler(IllegalStateException.class)
    public ApiResponse<ErrorResponse> handleIllegalStateException(Exception ex) {
        log.error("IllegalAccessException error: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        ApiResponse<ErrorResponse> apiResponse = new ApiResponse<>(false, null, errorResponse);
        return apiResponse;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            sb.append(error.getDefaultMessage()).append(" / ");
        });
        sb.setLength(sb.length() - 3);
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        ApiResponse<ErrorResponse> apiResponse = new ApiResponse<>(false, null, errorResponse);
        return apiResponse;
    }
}
