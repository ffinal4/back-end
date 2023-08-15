package com.example.peeppo.domain.rating.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingScoreResponseDto {

    private Long goodsId;
    private Long score;
}
