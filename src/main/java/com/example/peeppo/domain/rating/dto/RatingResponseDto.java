package com.example.peeppo.domain.rating.dto;

import lombok.Data;

@Data
public class RatingResponseDto {
    private String title;
    private String content;
    private String imageUrl;

    public RatingResponseDto(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}
