package com.example.peeppo.domain.user.entity;

import com.example.peeppo.domain.user.dto.SignupRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String location;

//    @Column(nullable = false)
//    private String emailImg;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public User(SignupRequestDto requestDto, String encodedPassword, UserRoleEnum role) {
        this.nickname = requestDto.getNickname();
        this.email = requestDto.getEmail();
        this.password = encodedPassword;
        this.role = role;
        this.location = requestDto.getLocation();
    }
}
