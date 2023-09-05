package com.example.peeppo.domain.goods.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class RequestAcceptRequestDto {

    @Size(min = 1, message = "교환할 물건을 선택해주세요")
    private List<Long> requestId = new ArrayList<>();

}
