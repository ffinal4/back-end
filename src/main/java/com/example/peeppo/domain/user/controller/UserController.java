package com.example.peeppo.domain.user.controller;

import com.example.peeppo.domain.user.dto.*;
import com.example.peeppo.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
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
    public ResponseDto logout(@RequestBody @Valid LogoutRequestDto logoutRequestDto,
                              HttpServletRequest req,
                              HttpServletResponse res) {
        return userService.logout(req, res, logoutRequestDto);
    }

    //회원정보 페이지
    @GetMapping("/users/{userId}")
    public ResponseEntity<MyPageResponseDto> mypage(@PathVariable Long userId) {
        return userService.mypage(userId);
    }

    @PatchMapping("/users/{userId}")
    public ResponseDto updatemypage(@PathVariable Long userId,
                                    @RequestPart(value = "data") @Valid MyPageRequestDto myPageRequestDto,
                                    @RequestPart(value = "image") MultipartFile multipartFile) throws IOException {
        return userService.updatemypage(userId, myPageRequestDto, multipartFile);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ResponseDto> deletemypage(@PathVariable Long userId) {
        return userService.deletemypage(userId);
    }
}
