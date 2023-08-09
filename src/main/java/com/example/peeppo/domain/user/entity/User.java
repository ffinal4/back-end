package com.example.peeppo.domain.user.entity;

import com.example.peeppo.domain.user.dto.MyPageRequestDto;
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
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String location;    //null 안되게 수정해둘것

    @Column(nullable = true)
    private String userImg;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public User(SignupRequestDto requestDto, String encodedPassword, UserRoleEnum role) {
        this.nickname = requestDto.getNickname();
        this.email = requestDto.getEmail();
        this.password = encodedPassword;
        this.role = role;
    }

    public void upload(MyPageRequestDto myPageRequestDto, String userImg, String encodedPassword) {
        this.nickname = myPageRequestDto.getNickname();
        this.password = myPageRequestDto.getPassword();
        this.location = myPageRequestDto.getLocation();
        this.userImg = userImg;
        this.password = encodedPassword;
    }
}
