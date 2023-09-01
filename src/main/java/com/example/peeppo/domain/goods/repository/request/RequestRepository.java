package com.example.peeppo.domain.goods.repository.request;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.entity.RequestGoods;
import com.example.peeppo.domain.goods.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<RequestGoods, Long> {

//    Page<RequestGoods> findByUserUserIdAndRequestStatus(Long userId, Pageable pageable, RequestStatus requestStatus);
//
//    Page<RequestGoods> findByUserUserId(Long userId, Pageable pageable);
//
//    RequestGoods findByGoodsGoodsId(Long goodsId);
//
//    List<RequestGoods> findByGoodsGoodsIdAndRequestStatus(Long goodsId, RequestStatus requestStatus);
}
