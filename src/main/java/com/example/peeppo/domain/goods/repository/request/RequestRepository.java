package com.example.peeppo.domain.goods.repository.request;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.entity.RequestGoods;
import com.example.peeppo.domain.goods.enums.RequestStatus;
import com.example.peeppo.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<RequestGoods, Long> {

  //  Page<RequestGoods> findByUserUserIdAndRequestStatus(Long userId, Pageable pageable, RequestStatus requestStatus);

 //   Page<RequestGoods> findByUserUserId(Long userId, Pageable pageable);

   // RequestGoods findByGoodsGoodsId(Long goodsId);

   // List<RequestGoods> findByGoodsGoodsIdAndRequestStatus(Long goodsId, RequestStatus requestStatus);

    @Query("SELECT DISTINCT r.seller FROM RequestGoods r WHERE r.user.userId = :user_id and r.seller.isDeleted = false and r.buyer.isDeleted = false ")
    Page<Goods> findSellerByUserId(@Param("user_id") Long userId, Pageable pageable);

    List<RequestGoods> findAllBySellerGoodsId(Long goodsId);

    @Query("SELECT DISTINCT r.seller FROM RequestGoods r WHERE r.receiveUser = :user_id and r.seller.isDeleted = false and r.buyer.isDeleted = false ")
    Page<Goods> findSellerByReceiveUser(@Param("user_id") Long userId, Pageable pageable);

    Optional<RequestGoods> findByBuyerGoodsId(Long id);

    @Query("SELECT r FROM RequestGoods r WHERE r.buyer.goodsId = :buyer_goods_id and r.seller.isDeleted = false and r.buyer.isDeleted = false ")
    RequestGoods findBuyerGoodsId(@Param("buyer_goods_id")Long id);

//    @Query("SELECT DISTINCT r.seller FROM RequestGoods r JOIN r.seller s WHERE r.user.userId = :user_id AND r.requestStatus = :request_status AND s.isDeleted = false")
    @Query("SELECT DISTINCT r.seller FROM RequestGoods r WHERE r.user.userId = :user_id and r.requestStatus= :request_status and r.seller.isDeleted = false")
    Page<Goods> findSellerByUserIdAndRequestStatus(@Param("user_id") Long userId,@Param("request_status")  RequestStatus requestStatus, Pageable pageable);

    @Query("SELECT r FROM RequestGoods r WHERE r.seller.goodsId = :seller_goods_id and r.seller.isDeleted = false and r.buyer.isDeleted = false ORDER BY r.requestId LIMIT 1 ")
    RequestGoods findBySellerGoodsId(@Param("seller_goods_id")Long id);

    @Query("SELECT DISTINCT r.seller FROM RequestGoods r WHERE r.receiveUser = :user_id and r.requestStatus= :request_status and r.seller.isDeleted = false and r.buyer.isDeleted = false ")
    Page<Goods> findSellerByReceiveUserAndRequestStatus(@Param("user_id") Long userId, @Param("request_status")  RequestStatus requestStatus, Pageable pageable);

    @Query("SELECT r FROM RequestGoods r WHERE r.seller.goodsId = :seller_goods_id and r.user.userId = :user_id and r.seller.isDeleted = false and r.buyer.isDeleted = false ")
    List<RequestGoods> findAllBySellerGoodsIdAndUserId(@Param("seller_goods_id") Long goodsId, @Param("user_id") Long userId);

//    @Query("SELECT r FROM RequestGoods r WHERE r.seller.goodsId = :seller_goods_id and r.receiveUser = :user_id ")
//    List<RequestGoods> findAllByBuyerGoodsIdAndUserId(@Param("seller_goods_id") Long goodsId, @Param("user_id") Long userId);

    @Query("SELECT r.user FROM RequestGoods r WHERE r.receiveUser = :user_id and r.seller.isDeleted = false and r.buyer.isDeleted = false ")
    Page<User> findByReceiveUser(@Param("user_id")Long userId, Pageable pageable);

    @Query("SELECT DISTINCT r.seller FROM RequestGoods r WHERE r.user.userId = :user_id and r.receiveUser = :user_id2 and r.seller.isDeleted = false and r.buyer.isDeleted = false ")
    List<Goods> findByBuyerUserAndSeller(@Param("user_id")Long userId, @Param("user_id2")Long userId1);

//    @Query("SELECT DISTINCT r.seller FROM RequestGoods r WHERE r.user.userId = :user_id and r.receiveUser = :user_id2 ")
//    List<RequestGoods> findByBuyerUserAndSeller(@Param("user_id")Long userId, @Param("user_id2")Long userId1);

    @Query("SELECT r.user FROM RequestGoods r WHERE r.receiveUser = :user_id and r.requestStatus= :request_status and r.seller.isDeleted = false and r.buyer.isDeleted = false ")
    Page<User> findByReceiveUserAndRequestStatus(@Param("user_id") Long userId , @Param("request_status") RequestStatus requestStatus, Pageable pageable);

    RequestGoods findBySellerAndUserUserId(Goods urGoods, Long userId);

    @Query("SELECT r FROM RequestGoods r WHERE r.buyer.goodsId = :buyer_goods_id and r.receiveUser= :user_id and r.seller.isDeleted = false and r.buyer.isDeleted = false ")
    RequestGoods findBuyerGoodsIdAndUserId((@Param("buyer_goods_id") Long requestGoodsId, @Param("user_id") Long userId);
}
