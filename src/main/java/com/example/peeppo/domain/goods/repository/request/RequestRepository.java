package com.example.peeppo.domain.goods.repository.request;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.entity.RequestGoods;
import com.example.peeppo.domain.goods.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestRepository extends JpaRepository<RequestGoods, Long> {

  //  Page<RequestGoods> findByUserUserIdAndRequestStatus(Long userId, Pageable pageable, RequestStatus requestStatus);

 //   Page<RequestGoods> findByUserUserId(Long userId, Pageable pageable);

   // RequestGoods findByGoodsGoodsId(Long goodsId);

   // List<RequestGoods> findByGoodsGoodsIdAndRequestStatus(Long goodsId, RequestStatus requestStatus);

    @Query("SELECT DISTINCT r.seller FROM RequestGoods r WHERE r.user.userId = :user_id")
    Page<Goods> findSellerByUserId(@Param("user_id") Long userId, Pageable pageable);

    List<RequestGoods> findAllBySellerGoodsId(Long goodsId);

    @Query("SELECT DISTINCT r.seller FROM RequestGoods r WHERE r.receiveUser = :user_id")
    Page<Goods> findSellerByReceiveUser(@Param("user_id") Long userId, Pageable pageable);

    RequestGoods findByBuyerGoodsId(Long id);

    @Query("SELECT DISTINCT r.seller FROM RequestGoods r WHERE r.user.userId = :user_id and r.requestStatus= :request_status")
    Page<Goods> findSellerByUserIdAndRequestStatus(@Param("user_id") Long userId,@Param("request_status")  RequestStatus requestStatus, Pageable pageable);

    RequestGoods findBySellerGoodsId(Long id);

    @Query("SELECT DISTINCT r.seller FROM RequestGoods r WHERE r.receiveUser = :user_id and r.requestStatus= :request_status")
    Page<Goods> findSellerByReceiveUserAndRequestStatus(@Param("user_id") Long userId, @Param("request_status")  RequestStatus requestStatus, Pageable pageable);
}
