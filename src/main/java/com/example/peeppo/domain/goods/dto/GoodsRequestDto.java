package com.example.peeppo.domain.goods.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
public class GoodsRequestDto {
    private String title;
    private String content;
    private List<MultipartFile> images;
    private String category;
    private String location;

    public GoodsRequestDto(String title, String content, List<MultipartFile> images, String category, String location) {
        this.title = title;
        this.content = content;
        this.images = images;
        this.category = category;
        this.location = location;
    }
}
