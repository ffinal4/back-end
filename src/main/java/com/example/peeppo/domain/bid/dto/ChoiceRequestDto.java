package com.example.peeppo.domain.bid.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class ChoiceRequestDto {

    @NotNull(message = "선호하는 물건을 선택해주세요")
    private List<Long> bidId;

    public List<Long> getbidId() {
        return bidId;
    }
}
