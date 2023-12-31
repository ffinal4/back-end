package com.example.peeppo.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RatingUserResponseDto {

    private String userImage;
    private String nickName;
    private Long maxRatingCount;
}
