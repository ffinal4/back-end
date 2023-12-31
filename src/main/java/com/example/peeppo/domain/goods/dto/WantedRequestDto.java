package com.example.peeppo.domain.goods.dto;

import com.example.peeppo.domain.goods.enums.Category;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WantedRequestDto {
    private String title;
    private String content;
    @NotNull
    private Category category;
}
