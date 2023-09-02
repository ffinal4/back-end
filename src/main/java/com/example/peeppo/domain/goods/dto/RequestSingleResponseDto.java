package com.example.peeppo.domain.goods.dto;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.enums.RequestStatus;
import com.example.peeppo.domain.image.entity.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class RequestSingleResponseDto {

    private Long userId;
    private Long goodsId;
    private String imageUrl;
    private String title;
    private String location;

    public RequestSingleResponseDto(Goods goods){
        this.imageUrl = goods.getImage().stream().map(Image::getImageUrl).collect(Collectors.toList()).get(0);
        this.title = goods.getTitle();
        this.location = goods.getLocation();
    }

    public RequestSingleResponseDto(Goods goods, Long userId) {
        this.userId = userId;
        this.goodsId = goods.getGoodsId();
        this.imageUrl = goods.getImage().stream().map(Image::getImageUrl).collect(Collectors.toList()).get(0);
        this.title = goods.getTitle();
        this.location = goods.getLocation();
    }


}
