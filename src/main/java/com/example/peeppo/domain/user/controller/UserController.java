package com.example.peeppo.domain.user.controller;

import com.example.peeppo.domain.user.dto.SignupRequestDto;
import com.example.peeppo.domain.user.service.UserService;
import com.example.peeppo.global.responseDto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ApiResponse<String> signup(@RequestBody @Valid SignupRequestDto requestDto) {

        return userService.signup(requestDto);
    }
}
