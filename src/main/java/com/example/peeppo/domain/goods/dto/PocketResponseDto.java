package com.example.peeppo.domain.goods.dto;

import com.example.peeppo.domain.goods.enums.GoodsStatus;
import com.example.peeppo.domain.user.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
public class PocketResponseDto {
    private Long userId;
    private String nickName;
    private String getUserImg;
    private String location;
    private Page<PocketListResponseDto> goodsListResponseDto;

    public PocketResponseDto(User user, Page<PocketListResponseDto> pocketListResponseDtos) {
        this.userId = user.getUserId();
        this.nickName = user.getNickname();
        this.getUserImg = user.getUserImg();
        this.goodsListResponseDto = pocketListResponseDtos;
    }
}
