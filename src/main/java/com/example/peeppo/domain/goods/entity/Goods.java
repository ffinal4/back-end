package com.example.peeppo.domain.goods.entity;

import com.example.peeppo.domain.goods.dto.GoodsRequestDto;
import com.example.peeppo.global.utils.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Goods extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goodsId;
    private String title;
    private String content;
    private String location;
    private String goodsCondition;
    private String tradeType;
    private String category;
    private Long userId; // 유저부분 완료 시 수정할 것
    private boolean isDeleted;
    @OneToOne(mappedBy = "goods", fetch = FetchType.LAZY)
    private WantedGoods wantedGoods;

    public Goods(GoodsRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.location = requestDto.getLocation();
        this.goodsCondition = requestDto.getGoodsCondition();
        this.tradeType = requestDto.getTradeType();
    }

    public void update(GoodsRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.location = requestDto.getLocation();
    }
}
