package com.example.peeppo.domain.goods.repository;

import com.example.peeppo.domain.goods.entity.WantedGoods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WantedGoodsRepository extends JpaRepository<WantedGoods, Long> {
}