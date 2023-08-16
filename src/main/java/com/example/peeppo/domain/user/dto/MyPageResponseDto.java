package com.example.peeppo.domain.user.dto;

import com.example.peeppo.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MyPageResponseDto {

    private String nickname;
    private String email;
    private String password;
    private String location;
    private Long userPoint;

    public MyPageResponseDto(User user) {
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.location = user.getLocation();
        this.userPoint = user.getUserPoint();
    }
}
