package com.example.peeppo.domain.goods.entity;

import com.example.peeppo.domain.goods.dto.GoodsRequestDto;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.global.utils.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;


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
    private boolean isDeleted;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Goods(GoodsRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.location = requestDto.getLocation();
        this.goodsCondition = requestDto.getGoodsCondition();
        this.tradeType = requestDto.getTradeType();
        this.user = user;
    }

    public void update(GoodsRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.location = requestDto.getLocation();
    }
}
