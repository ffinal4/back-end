package com.example.peeppo.domain.rating.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopRatingUserResponseDto {
    private String nickName;
    private Long maxRatingCount;
}
