package com.example.peeppo.domain.user.service;

import com.example.peeppo.domain.image.service.UploadService;
import com.example.peeppo.domain.user.dto.*;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.entity.UserRoleEnum;
import com.example.peeppo.domain.user.repository.UserRepository;
import com.example.peeppo.global.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTemplate redisTemplate;
    private final UploadService uploadService;

    public ResponseEntity<ResponseDto> signup(SignupRequestDto signupRequestDto) {
        String email = signupRequestDto.getEmail();
        boolean validateDuplicateEmail = userRepository.findByEmail(email).isEmpty();

        if (!validateDuplicateEmail) {
            ResponseDto response = new ResponseDto("중복된 이메일입니다.", HttpStatus.BAD_REQUEST.value(), "BAD");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
        }

        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        UserRoleEnum role = UserRoleEnum.USER;
        User user = new User(signupRequestDto, encodedPassword, role);

        userRepository.save(user);

        ResponseDto response = new ResponseDto("회원가입 완료", HttpStatus.OK.value(), "OK");
        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    //닉네임 중복체크
    public ResponseEntity<CheckResponseDto> checkValidateNickname(ValidateRequestDto validateRequestDto) {
        String nickname = validateRequestDto.getNickname();
        boolean validateDuplicateNickname = isDuplicatedNickname(nickname);

        if (!validateDuplicateNickname) {
            throw new IllegalStateException("중복된 이름입니다.");
        }

        CheckResponseDto response = new CheckResponseDto("중복되지 않은 이름입니다.", validateDuplicateNickname, OK.value(), "OK");
        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    private boolean isDuplicatedNickname(String nickname) {
        return userRepository.findByNickname(nickname).isEmpty();
    }

    public ResponseDto logout(HttpServletRequest req, HttpServletResponse res, LogoutRequestDto logoutRequestDto) {
        // 1. Access Token 검증
        if (!jwtUtil.validateToken(req, res, logoutRequestDto.getAccessToken())) {
            return new ResponseDto("잘못된 요청입니다.", HttpStatus.BAD_REQUEST.value(), "BAD");
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtUtil.getAuthentication(logoutRequestDto.getAccessToken());

        // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get(authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete(authentication.getName());
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtUtil.getExpiration(logoutRequestDto.getAccessToken());
        redisTemplate.opsForValue()
                .set(logoutRequestDto.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        return new ResponseDto("로그아웃 되었습니다.", HttpStatus.OK.value(), "OK");
    }

    //회원정보 페이지
    public ResponseEntity<MyPageResponseDto> myPage(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        MyPageResponseDto myPageResponseDto = new MyPageResponseDto(user);

        return ResponseEntity.status(HttpStatus.OK.value()).body(myPageResponseDto);
    }

    public ResponseDto updatemypage(Long userId, MyPageRequestDto myPageRequestDto, MultipartFile multipartFile) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        String encodedPassword = passwordEncoder.encode(myPageRequestDto.getPassword());
        String updateUserImg = uploadService.upload(multipartFile);
        user.upload(myPageRequestDto, updateUserImg, encodedPassword);

        userRepository.save(user);

        return new ResponseDto("개인정보가 수정되었습니다.", HttpStatus.OK.value(), "OK");
    }

    public ResponseEntity<ResponseDto> deletemypage(Long userId, User user1) throws IllegalAccessException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        if(user.getUserId().equals(user1.getUserId())) {
            userRepository.deleteById(userId);
        }
        else {
            throw new IllegalAccessException();
        }

        ResponseDto response = new ResponseDto("탈퇴 완료", HttpStatus.OK.value(), "OK");
        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }
}
