package com.example.peeppo.domain.dibs.repository;

import com.example.peeppo.domain.dibs.entity.Dibs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DibsRepository extends JpaRepository<Dibs, Long> {
    Optional<Dibs> findByUserUserIdAndGoodsGoodsId(Long userId, Long goodsId);

    List<Dibs> findByUserUserId(Long userId);

    Long countByGoodsGoodsId(Long goodsId);
}
