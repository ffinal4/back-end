package com.example.peeppo.domain.goods.entity;

import com.example.peeppo.domain.goods.dto.GoodsRequestDto;
import com.example.peeppo.global.utils.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Goods extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goodsId;
    private String title;
    private String content;
    private String image;
    private String category;
    private String location;
    private boolean isDeleted;

    public Goods(GoodsRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.image = requestDto.getImage();
        this.category = requestDto.getCategory();
        this.location = requestDto.getLocation();
    }

    public void update(GoodsRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.image = requestDto.getImage();
        this.category = requestDto.getCategory();

    }
}
