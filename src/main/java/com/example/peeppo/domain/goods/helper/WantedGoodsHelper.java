package com.example.peeppo.domain.goods.helper;

import com.example.peeppo.domain.goods.entity.WantedGoods;
import com.example.peeppo.domain.goods.repository.WantedGoodsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WantedGoodsHelper {
        private final WantedGoodsRepository wantedGoodsRepository;
        public WantedGoods findWantedGoods(Long goodsId) {
            return wantedGoodsRepository.findById(goodsId).orElseThrow(() ->
                    new NullPointerException("해당 게시글은 존재하지 않습니다."));
        }
}
