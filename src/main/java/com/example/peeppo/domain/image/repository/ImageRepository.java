package com.example.peeppo.domain.image.repository;

import com.example.peeppo.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, String> {
    List<Image> findByGoodsGoodsId(Long goodsId);
    Image findByImageUrl(String imageUrl);

    Image findFirstByGoodsGoodsIdOrderByCreatedAtAsc(Long goodsId);
}
