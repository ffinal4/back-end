package com.example.peeppo.domain.user.dto;

import com.example.peeppo.domain.user.entity.User;

public class MyPageResponseDto {

    String nickname;
    String email;
    String password;
    String location;

    public MyPageResponseDto(User user) {
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.location = user.getLocation();
    }
}
