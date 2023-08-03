package com.example.peeppo.domain.goods.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteResponseDto {
    String msg;

    public DeleteResponseDto(String msg) {
        this.msg = msg;
    }
}
