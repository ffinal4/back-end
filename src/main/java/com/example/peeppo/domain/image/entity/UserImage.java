package com.example.peeppo.domain.image.entity;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.ToOne;

@Entity
@Getter
@NoArgsConstructor
public class UserImage {

    @Id
    private String imageKey;

    @Column(nullable = false)
    private String imageUrl;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false) // 연결할 외래키 컬럼명
    @JsonBackReference
    private User user;

    public UserImage(String imageKey, String image, User user) {
        this.imageKey = imageKey;
        this.imageUrl = image;
        this.user = user;
    }
    public UserImage(UserImage userImage){
        this.imageKey = userImage.getImageKey();
        this.imageUrl = userImage.imageUrl;
        this.user = userImage.getUser();
    }
}
