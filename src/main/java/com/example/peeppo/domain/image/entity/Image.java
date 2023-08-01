package com.example.peeppo.domain.image.entity;


import com.example.peeppo.domain.goods.entity.Goods;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Image {
    @Id
    private String imageKey;

    @Column(nullable = false)
    private String image;

    @ManyToOne
    @JoinColumn(name = "goods_id") // 연결할 외래키 컬럼명
    private Goods goods;

    public Image(String imageKey, String image, Goods goods) {
        this.imageKey = imageKey;
        this.image = image;
        this.goods = goods;
    }
}
