package com.example.peeppo.domain.image.repository;

import com.example.peeppo.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByGoodsGoodsId(Long goodsId);
    List<Image> deleteByGoodsGoodsId(Long goodsId);
}
