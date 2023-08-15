package com.example.peeppo.domain.rating.scheduler;


import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.repository.GoodsRepository;
import com.example.peeppo.domain.rating.helper.RatingHelper;
import com.example.peeppo.domain.rating.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RatingScheduler {
    private final GoodsRepository goodsRepository;
    private final RatingHelper ratingHelper;

    private final static int TRANSACTION_CHUNK_SIZE = 50;

    @Scheduled(cron = "0 0 6 * * SUN") // 매주 일요일 새벽 6시에 실행
    public void resetPrices()  {
        long cursor = 0L;
        long goodsListSize = 0;

        do {
            List<Goods> goodsList = goodsRepository
                    .findAllByCursor(cursor, TRANSACTION_CHUNK_SIZE);
            goodsListSize = goodsList.size();

            for (Goods goods : goodsList) {
                ratingHelper.resetGoodsAvgPrice(goods);
                cursor = goods.getGoodsId();
            }

        } while (goodsListSize == TRANSACTION_CHUNK_SIZE);
    }
}
