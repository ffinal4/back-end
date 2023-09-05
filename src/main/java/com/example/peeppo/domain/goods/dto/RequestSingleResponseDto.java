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
        this.userId = goods.getUser().getUserId();
        this.goodsId = goods.getGoodsId();
        this.imageUrl = goods.getImage().get(0).getImageUrl();
        this.title = goods.getTitle();
        this.location = goods.getLocation();
    }
}
