package com.example.peeppo.domain.image.entity;


import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.global.utils.Timestamped;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Image extends Timestamped {

    // Long으로 고칠 것
    @Id
    private String imageKey;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "goods_id", nullable = false) // 연결할 외래키 컬럼명
    @JsonBackReference
    private Goods goods;

    public Image(String imageKey, String image, Goods goods) {
        this.imageKey = imageKey;
        this.imageUrl = image;
        this.goods = goods;
    }
}
