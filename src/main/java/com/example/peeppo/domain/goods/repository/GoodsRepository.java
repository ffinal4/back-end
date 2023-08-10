package com.example.peeppo.domain.goods.repository;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
    Page<Goods> findAllByIsDeletedFalse(Pageable pageable);
    Page<Goods> findAllByUserAndIsDeletedFalse(User user, Pageable pageable);

   // Page<Goods> findGoodsByUser(@Param("userId") Long userId);


//    List<Goods> findAllByLocationIdAndIsDeletedFalseOrderByGoodsIdDesc(Long locationId);


}
