package com.example.peeppo.domain.goods.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoodsRequestDto {
    private String title;
    private String content;
    private String category;
    private String location;
    private List<MultipartFile> images;
    private String goodsCondition;
    private String tradeType;

}
