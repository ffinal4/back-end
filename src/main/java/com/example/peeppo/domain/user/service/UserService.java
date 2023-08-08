package com.example.peeppo.domain.user.service;

import com.example.peeppo.domain.user.dto.SignupRequestDto;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.entity.UserRoleEnum;
import com.example.peeppo.domain.user.repository.UserRepository;
import com.example.peeppo.global.responseDto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private String ADMIN_TOKEN;


    @Transactional
    public ApiResponse<String> signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String nickname = requestDto.getNickname();
        // 회원 중복 확인
        checkUsername(username);
        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }
        User user = new User(username, password, nickname, role);
        userRepository.save(user);
        return new ApiResponse<>(true, "회원가입 성공!", null);
    }

    private void checkUsername(String username) {
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }
    }
}

