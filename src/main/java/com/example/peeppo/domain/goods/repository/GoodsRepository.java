package com.example.peeppo.domain.goods.repository;

import com.example.peeppo.domain.goods.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
    List<Goods> findAllByIsDeletedFalseOrderByGoodsIdDesc();

    List<Goods> findAllByLocationIdAndIsDeletedFalseOrderByGoodsIdDesc(Long locationId);


}
