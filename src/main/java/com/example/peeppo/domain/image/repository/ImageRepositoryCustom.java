package com.example.peeppo.domain.image.repository;

import com.example.peeppo.domain.image.entity.Image;

import java.util.List;

public interface ImageRepositoryCustom {

    List<Image> findByGoodsGoodsIdOrderByCreatedAtAsc(Long goodsId);

    Image findByGoodsGoodsIdOrderByCreatedAtAscFirst(Long goodsId);
}
