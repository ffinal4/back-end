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
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String emailImg;

    public User(SignupRequestDto requestDto, String encodedPassword) {
        this.nickname = requestDto.getNickname();
        this.name = requestDto.getName();
        this.email = requestDto.getEmail();
        this.password = encodedPassword;
    }
}
