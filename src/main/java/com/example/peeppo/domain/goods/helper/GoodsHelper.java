package com.example.peeppo.domain.goods.helper;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.repository.GoodsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoodsHelper {
    private final GoodsRepository goodsRepository;
    public Goods findGoods(Long goodsId) {
        Goods goods = goodsRepository.findById(goodsId).orElseThrow(() ->
                new NullPointerException("해당 게시글은 존재하지 않습니다."));
        if (goods.isDeleted()) {
            throw new IllegalStateException("삭제된 게시글입니다.");
        }
        return goods;
    }

}
