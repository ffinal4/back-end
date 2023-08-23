package com.example.peeppo.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RatingUserResponseDto {
    private String nickName;
    private Long maxRatingCount;
}
