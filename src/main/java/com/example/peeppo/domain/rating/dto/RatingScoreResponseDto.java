package com.example.peeppo.domain.rating.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingScoreResponseDto {

    private Long goodsId;
    private Long currentPoint;
    private Long score;
    private Long ratingPrice;
    private Long sellerPrice;
    private Long totalPoint;


}
