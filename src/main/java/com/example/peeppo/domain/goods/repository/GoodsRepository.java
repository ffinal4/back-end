package com.example.peeppo.domain.goods.repository;

import com.example.peeppo.domain.goods.entity.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
    Page<Goods> findAllByIsDeletedFalse(Pageable pageable);

//    List<Goods> findAllByLocationIdAndIsDeletedFalseOrderByGoodsIdDesc(Long locationId);


}
