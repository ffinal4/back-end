package com.example.peeppo.domain.rating.repository.ratingGoodsRepository;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.rating.entity.RatingGoods;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RatingGoodsRepository extends JpaRepository<RatingGoods, Long>{

    Optional<RatingGoods> findByGoods(Goods goods);

    @Lock(LockModeType.OPTIMISTIC)
    @Query(value = "select r from RatingGoods r where r.goods.goodsId = :goodsId")
    Optional<RatingGoods> findByGoodsForUpdate(@Param("goodsId") Long goodsId);

}
