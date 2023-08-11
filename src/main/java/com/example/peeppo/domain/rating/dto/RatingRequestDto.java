package com.example.peeppo.domain.rating.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RatingRequestDto {
    private Long goodsId;
    private Long ratingPrice;
    private Long ratingCount;
}
