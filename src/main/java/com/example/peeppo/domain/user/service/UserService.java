package com.example.peeppo.domain.user.service;

import com.example.peeppo.domain.user.dto.EmailCheckResponseDto;
import com.example.peeppo.domain.user.dto.LoginRequestDto;
import com.example.peeppo.domain.user.dto.ResponseDto;
import com.example.peeppo.domain.user.dto.SignupRequestDto;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<ResponseDto> signup(SignupRequestDto signupRequestDto) {
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        User user = new User(signupRequestDto, encodedPassword);

        userRepository.save(user);

        ResponseDto response = new ResponseDto("회원가입 완료", HttpStatus.OK.value(), "OK");
        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    public ResponseEntity<EmailCheckResponseDto> checkValidate(SignupRequestDto signupRequestDto) {
        String email = signupRequestDto.getEmail();
        boolean validateDuplicateEmail = isDuplicatedEmail(email);
        EmailCheckResponseDto response = new EmailCheckResponseDto("중복되지 않은 이메일입니다.", validateDuplicateEmail, OK.value(), "OK");
        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    private boolean isDuplicatedEmail(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    //나중에 지워둘 것
    public ResponseDto login(LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("등록된 회원을 찾을 수 없습니다.")
        );

        return new ResponseDto("success", HttpStatus.OK.value(), "OK");
    }
}
