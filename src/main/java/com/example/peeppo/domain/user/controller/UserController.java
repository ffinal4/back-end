package com.example.peeppo.domain.user.controller;


import com.example.peeppo.domain.user.dto.EmailCheckResponseDto;
import com.example.peeppo.domain.user.dto.LoginRequestDto;
import com.example.peeppo.domain.user.dto.ResponseDto;
import com.example.peeppo.domain.user.dto.SignupRequestDto;
import com.example.peeppo.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "Signup")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/users/signup")
    public ResponseEntity<ResponseDto> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

    @PostMapping("/users/email")
    public ResponseEntity<EmailCheckResponseDto> checkValidate(@RequestBody SignupRequestDto signupRequestDto) {
        return userService.checkValidate(signupRequestDto);
    }

    //나중에 지워둘 것
    @ResponseBody
    @PostMapping("/users/login")
    public ResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto);
    }
}
