package com.example.peeppo.domain.user.service;

import com.example.peeppo.domain.image.entity.UserImage;
import com.example.peeppo.domain.image.helper.ImageHelper;
import com.example.peeppo.domain.image.repository.UserImageRepository;
import com.example.peeppo.domain.user.dto.*;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.entity.UserRoleEnum;
import com.example.peeppo.domain.user.helper.UserRatingHelper;
import com.example.peeppo.domain.user.repository.UserRepository;
import com.example.peeppo.global.responseDto.ApiResponse;
import com.example.peeppo.global.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTemplate redisTemplate;
    private final ImageHelper imageHelper;
    private final UserImageRepository userImageRepository;
    private final UserRatingHelper userRatingHelper;

    public ResponseDto signup(SignupRequestDto signupRequestDto) {

        String email = signupRequestDto.getEmail();
        boolean validateDuplicateEmail = userRepository.findByEmail(email).isEmpty();

        if (!validateDuplicateEmail) {
            return new ResponseDto("중복된 이메일입니다.", HttpStatus.BAD_REQUEST.value(), "BAD");
        }

        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        UserRoleEnum role = UserRoleEnum.USER;
        User user = new User(signupRequestDto, encodedPassword, role);

        userRepository.save(user);

        return new ResponseDto("회원가입 완료", HttpStatus.OK.value(), "OK");
    }

    //닉네임 중복체크
    public CheckResponseDto checkValidateNickname(ValidateRequestDto validateRequestDto) {
        String nickname = validateRequestDto.getNickname();
        boolean validateDuplicateNickname = isDuplicatedNickname(nickname);
        if (!validateDuplicateNickname) {
            throw new IllegalStateException("중복된 이름입니다.");
        }
        return new CheckResponseDto("중복되지 않은 이름입니다.", validateDuplicateNickname, OK.value(), "OK");
    }

    public ApiResponse<ResponseDto> checkValidatePassword(PasswordRequestDto passwordRequestDto, User user) {
        if (!passwordEncoder.matches(passwordRequestDto.getOriginPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String encodedPassword = user.getPassword();
        if (null != passwordRequestDto.getPassword()) {
            encodedPassword = passwordEncoder.encode(passwordRequestDto.getPassword());
        }

        user.uploadPassword(encodedPassword);
        userRepository.save(user);

        return new ApiResponse<>(true, new ResponseDto("개인정보가 수정되었습니다.", HttpStatus.OK.value(), "OK"), null);
    }

    private boolean isDuplicatedNickname(String nickname) {
        return userRepository.findByNickname(nickname).isEmpty();
    }

    public ResponseDto logout(HttpServletRequest req, HttpServletResponse res, LogoutRequestDto logoutRequestDto) {
        String accessToken = jwtUtil.substringToken(logoutRequestDto.getAccessToken());
        // 1. Access Token 검증
        if (!jwtUtil.validateToken(req, res, accessToken)) {
            return new ResponseDto("잘못된 요청입니다.", HttpStatus.BAD_REQUEST.value(), "BAD");
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtUtil.getAuthentication(accessToken);

        // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get(authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete(authentication.getName());
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtUtil.getExpiration(accessToken);
        redisTemplate
                .opsForValue()
                .set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);

        return new ResponseDto("로그아웃 되었습니다.", HttpStatus.OK.value(), "OK");
    }

    //회원정보 페이지
    public MyPageResponseDto myPage(User user) {
        userRatingHelper.getUser(user.getUserId());
        Optional<UserImage> userImage = userImageRepository.findByUserUserId(user.getUserId());
        String imageUrl = userImage.map(UserImage::getImageUrl).orElse(null);

        return new MyPageResponseDto(user, imageUrl);
    }

    @Transactional
    public ApiResponse<ResponseDto> updateMyPage(MyPageRequestDto myPageRequestDto, MultipartFile multipartFile, User user) throws IOException {

        userRatingHelper.getUser(user.getUserId());

        if (multipartFile != null) {
            imageHelper.deleteUserImages(user);
            imageHelper.saveUserImages(multipartFile, user);
        }

        user.upload(myPageRequestDto);
        userRepository.save(user);

        return new ApiResponse<>(true, new ResponseDto("개인정보가 수정되었습니다.", HttpStatus.OK.value(), "OK"), null);
    }

    public ResponseDto deleteMyPage(Long userId) {
        userRepository.deleteById(userId);

        return new ResponseDto("탈퇴 완료되었습니다.", HttpStatus.OK.value(), "OK");
    }
}
