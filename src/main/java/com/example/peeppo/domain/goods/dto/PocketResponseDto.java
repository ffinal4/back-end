package com.example.peeppo.domain.goods.dto;

import com.example.peeppo.domain.goods.enums.GoodsStatus;
import com.example.peeppo.domain.user.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PocketResponseDto {
    private Long userId;
    private String nickName;
    private String getUserImg;
    private String location;
    private List<GoodsListResponseDto> goodsListResponseDto;
    private GoodsStatus goodsStatus;

    public PocketResponseDto(User user, List<GoodsListResponseDto> goodsListResponseDto) {
        this.userId = user.getUserId();
        this.nickName = user.getNickname();
        this.getUserImg = user.getUserImg();
        this.location = user.getLocation();
        this.goodsListResponseDto = goodsListResponseDto;
    }
}
