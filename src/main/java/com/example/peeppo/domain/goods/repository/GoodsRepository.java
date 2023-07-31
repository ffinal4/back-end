package com.example.peeppo.domain.goods.repository;

import com.example.peeppo.domain.goods.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
}
