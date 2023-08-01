package com.example.peeppo.domain.goods.dto;

import lombok.Getter;

@Getter
public class GoodsRequestDto {
    private String title;
    private String content;
    private String image;
    private String category;
    private String location;

    public GoodsRequestDto(GoodsRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.image = requestDto.getImage();
        this.category = requestDto.getCategory();
        this.location = requestDto.getLocation();
    }
}
