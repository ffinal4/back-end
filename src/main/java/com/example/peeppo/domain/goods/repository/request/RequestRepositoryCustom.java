package com.example.peeppo.domain.goods.repository.request;

import com.example.peeppo.domain.goods.entity.RequestGoods;
import com.example.peeppo.domain.goods.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RequestRepositoryCustom {
    //    List<RequestGoods> findAllByBuyerGoodsIdAndUserId(Long sellerGoodsId, Long userId);
    List<RequestGoods> findByReceiveUserGroupBySeller(Long receiverUser, Pageable pageable);

    Page<RequestGoods> findBySellerGoodsIdAndUserIdGroup(Long receiveUser, Pageable pageable);

    List<RequestGoods> findBySellerGoodsIdAndGroup(Long receiverUser, Long goodsId, Long buyerId);

    Page<RequestGoods> findBySellerGoodsIdAndUserIdGroupAndStatus(Long receiveUser, Pageable pageable, RequestStatus requestStatus);
}
