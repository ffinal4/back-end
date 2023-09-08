package com.example.peeppo.global.scheduler;


import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.repository.goods.GoodsRepository;
import com.example.peeppo.domain.rating.helper.RatingHelper;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.peeppo.domain.goods.enums.GoodsStatus.ONSALE;

@Component
@DynamicUpdate
@RequiredArgsConstructor
public class RatingScheduler {
    private final GoodsRepository goodsRepository;
    private final RatingHelper ratingHelper;

    private final static int TRANSACTION_CHUNK_SIZE = 50;

    @Scheduled(cron = "0 0 6 * * *") // 매일 새벽 6시에 가격 재설정
    public void resetPrices() {
        long cursor = 0L;
        long goodsListSize = 0;

        do {
            List<Goods> goodsList = goodsRepository
                    .findAllByCursor(cursor, TRANSACTION_CHUNK_SIZE);
            goodsListSize = goodsList.size();

            for (Goods goods : goodsList) {
                if (ONSALE.equals(goods.getGoodsStatus())) {
                    ratingHelper.resetGoodsAvgPrice(goods);
                    cursor = goods.getGoodsId();
                }
            }

        } while (goodsListSize == TRANSACTION_CHUNK_SIZE);
    }
}
