package com.example.peeppo.domain.user.controller;

import com.example.peeppo.domain.auction.dto.AuctionResponseDto;
import com.example.peeppo.domain.user.dto.*;
import com.example.peeppo.domain.user.service.UserService;
import com.example.peeppo.global.responseDto.ApiResponse;
import com.example.peeppo.global.security.UserDetailsImpl;
import com.example.peeppo.global.utils.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/users/signup")
    public ApiResponse<ResponseDto> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        return ResponseUtils.ok(userService.signup(signupRequestDto));
    }

    //닉네임 중복확인
    @PostMapping("/users/nickname")
    public ApiResponse<CheckResponseDto> checkValidateNickname(@RequestBody @Valid ValidateRequestDto validateRequestDto) {
        return ResponseUtils.ok(userService.checkValidateNickname(validateRequestDto));
    }

    @PostMapping("/users/password")
    public ApiResponse<ResponseDto> checkValidatePassword(@RequestBody @Valid PasswordRequestDto passwordRequestDto,
                                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.checkValidatePassword(passwordRequestDto, userDetails.getUser());
    }

    @PostMapping("/users/logout")
    public ApiResponse<ResponseDto> logout(@RequestBody @Valid LogoutRequestDto logoutRequestDto,
                                           HttpServletRequest req,
                                           HttpServletResponse res) {
        return ResponseUtils.ok(userService.logout(req, res, logoutRequestDto));
    }

    //회원정보 페이지
    @GetMapping("/users/mypage")
    public ApiResponse<MyPageResponseDto> myPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseUtils.ok(userService.myPage(userDetails.getUser()));
    }

    @PatchMapping("/users/mypage")
    public ApiResponse<ResponseDto> updateMyPage(@RequestPart(value = "data") @Valid MyPageRequestDto myPageRequestDto,
                                                        @RequestPart(value = "image") MultipartFile multipartFile,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return userService.updateMyPage(myPageRequestDto, multipartFile, userDetails.getUser());
    }

    @DeleteMapping("/users")
    public ApiResponse<ResponseDto> deleteMyPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseUtils.ok(userService.deleteMyPage(userDetails.getUser().getUserId()));
    }
}
