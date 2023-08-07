package com.example.peeppo.domain.user.controller;

import com.example.peeppo.domain.user.dto.CheckResponseDto;
import com.example.peeppo.domain.user.dto.LogoutRequestDto;
import com.example.peeppo.domain.user.dto.ResponseDto;
import com.example.peeppo.domain.user.dto.SignupRequestDto;
import com.example.peeppo.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/users/signup")
    public ResponseEntity<ResponseDto> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

    //닉네임 중복확인
    @PostMapping("/users/nickname")
    public ResponseEntity<CheckResponseDto> checkValidateNickname(@RequestBody SignupRequestDto signupRequestDto) {
        return userService.checkValidateNickname(signupRequestDto);
    }

    @PostMapping("/users/logout")
    public ResponseDto logout(@RequestBody @Valid LogoutRequestDto logoutRequestDto, HttpServletRequest req, HttpServletResponse res) {
        return userService.logout(req, res, logoutRequestDto);
    }
}
