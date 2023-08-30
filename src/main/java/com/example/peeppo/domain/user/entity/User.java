package com.example.peeppo.domain.user.entity;

import com.example.peeppo.domain.chat.entity.ChatRoom;
import com.example.peeppo.domain.image.entity.Image;
import com.example.peeppo.domain.user.dto.MyPageRequestDto;
import com.example.peeppo.domain.user.dto.SignupRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@DynamicUpdate
@Slf4j
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

    private String userImg;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @OneToMany(mappedBy = "user")
    private List<ChatRoom> chatRoom;

    @Column
    private Long maxRatingCount = 0L;
    private Long currentRatingCount = 0L;
    // 한 게임에서 얻은 총 포인트
    private Long totalPoint = 0L;


    @Column
    private Long userPoint = 0L;

    public User(SignupRequestDto requestDto, String encodedPassword, UserRoleEnum role) {
        this.nickname = requestDto.getNickname();
        this.email = requestDto.getEmail();
        this.password = encodedPassword;
        this.role = role;
        this.location = requestDto.getLocation();
    }

    public void upload(MyPageRequestDto myPageRequestDto, String userImg, String encodedPassword) {
        this.nickname = myPageRequestDto.getNickname();
        this.location = myPageRequestDto.getLocation();
        this.userImg = userImg;
        this.password = encodedPassword;
    }

    public void countUpdate(Long userPoint, Long currentCount){
        this.currentRatingCount = currentCount;

        if(this.maxRatingCount < currentCount){
            this.maxRatingCount = currentCount;
        }

        this.userPoint += userPoint;
        this.totalPoint += userPoint;
    }

    public void totalPointInit(){
        this.totalPoint = 0L;
    }

    public void imgUpdate(String image){
        this.userImg = image;
    }

    public void userPointAdd(Long userPoint) {
        this.userPoint += userPoint;
    }
    public void userPointSubtract(Long userPoint) {
        this.userPoint -= userPoint;
    }

}
