package com.example.peeppo.domain.bid.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ChoiceRequestDto {
    private List<Long> bidId;

    public List<Long> getbidId() {
        return bidId;
    }
}
