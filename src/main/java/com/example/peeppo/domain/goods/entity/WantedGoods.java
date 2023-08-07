package com.example.peeppo.domain.goods.entity;

import com.example.peeppo.domain.goods.dto.WantedRequestDto;
import com.example.peeppo.domain.goods.enums.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class WantedGoods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wantedId;

    private String title;
    private String content;
    @Enumerated(EnumType.STRING)
    private Category category;

    public WantedGoods(WantedRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.category = requestDto.getCategory();
    }

    public void update(WantedRequestDto wantedRequestDto) {
        this.title = wantedRequestDto.getTitle();
        this.content = wantedRequestDto.getContent();
        this.category = wantedRequestDto.getCategory();

    }
}