package com.example.peeppo.domain.goods.repository;

import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.goods.entity.Goods;

import java.util.List;

public interface GoodsRepositoryCustom {

    List<Goods> findAllByCursor(long cursor, int limit);
//    Goods findRandomGoods(User targetUser);
}
