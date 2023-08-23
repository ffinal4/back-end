package com.example.peeppo.domain.auction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuctionRequestDto {

    @NotBlank
    String endTime;

    @NotBlank
    Double lowPrice;
}
