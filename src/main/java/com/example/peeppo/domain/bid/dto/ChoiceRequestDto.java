package com.example.peeppo.domain.bid.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ChoiceRequestDto {

    @NotNull(message = "물품을 선택해주세요")
    private List<Long> bidId;

}
