package com.example.peeppo.domain.goods.dto;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.image.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoodsRecentDto {
    // 사진 , 아이디
    private Long id;
    private String image;

    public GoodsRecentDto(Goods goods) {
        this.id = goods.getGoodsId();
        this.image = goods.getImage().stream().map(Image::getImageUrl).toList().get(0);
    }
}
