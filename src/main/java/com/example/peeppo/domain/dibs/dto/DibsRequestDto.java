package com.example.peeppo.domain.dibs.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DibsRequestDto {

    @NotNull
    private Long goodsId;
}
