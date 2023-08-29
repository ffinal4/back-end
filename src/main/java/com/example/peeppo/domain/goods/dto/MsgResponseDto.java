package com.example.peeppo.domain.goods.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MsgResponseDto {
    String msg;

    public MsgResponseDto(String msg) {
        this.msg = msg;
    }


}
