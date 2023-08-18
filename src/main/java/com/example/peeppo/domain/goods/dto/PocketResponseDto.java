package com.example.peeppo.domain.goods.dto;

import com.example.peeppo.domain.user.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PocketResponseDto {
    private Long userId;
    private String avgSeller;
    private String getUserImg;
    private List<PocketListResponseDto> goodsListResponseDto;

    public PocketResponseDto(User user, List<PocketListResponseDto> pocketListResponseDtos) {
        this.userId = user.getUserId();
        this.avgSeller = user.getNickname();
        this.getUserImg = user.getUserImg();
        this.goodsListResponseDto = pocketListResponseDtos;
    }
}
