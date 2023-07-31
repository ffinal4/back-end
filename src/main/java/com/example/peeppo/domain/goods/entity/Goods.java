package com.example.peeppo.domain.goods.entity;

import com.example.peeppo.domain.goods.dto.goodsRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goodsId;
    private String title;
    private String content;
    private String image;
    private String category;
    private String location;

    public Goods(goodsRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.image = requestDto.getImage();
        this.category = requestDto.getCategory();
        this.location = requestDto.getLocation();
    }
}
