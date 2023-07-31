package com.example.peeppo.domain.goods.dto;

import lombok.Getter;

@Getter
public class goodsRequestDto {
    private String title;
    private String content;
    private String image;
    private String category;
    private String location;

    public goodsRequestDto(goodsRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.image = requestDto.getImage();
        this.category = requestDto.getCategory();
        this.location = requestDto.getLocation();
    }
}
