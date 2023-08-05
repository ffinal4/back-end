package com.example.peeppo.domain.goods.entity;

import com.example.peeppo.domain.goods.dto.WantedRequestDto;
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
    private String category;
    @OneToOne
    @JoinColumn(name = "goods_id")
    private Goods goods;

    public WantedGoods(WantedRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
}