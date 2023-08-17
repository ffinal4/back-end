package com.example.peeppo.domain.goods.repository;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.enums.GoodsStatus;
import com.example.peeppo.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface GoodsRepository extends JpaRepository<Goods, Long>, GoodsRepositoryCustom{
    Page<Goods> findAllByIsDeletedFalse(Pageable pageable);
    Page<Goods> findAllByUserAndIsDeletedFalse(User user, Pageable pageable);

    @Query(value = "select g1.* " +
            "from goods g1 " +
            "where g1.goods_id not in " +
            "(select g2.goods_id " +
            "from goods g2 " +
            "inner join rating_goods rg on rg.goods_id = g2.goods_id " +
            "inner join rating r on r.rating_goods_id = rg.rating_goods_id " +
            "inner join user_rating_relation urr on urr.rating_id = r.rating_id " +
            "inner join user u on u.user_id = urr.user_id " +
            "where u.user_id = :#{#targetUser.userId} " +
            "group by g2.goods_id " +
            "having COUNT(r.rating_id) <= 3) " +
            "and g1.user_id <> :#{#targetUser.userId} " +
            "and g1.is_deleted = false " +
            "order by rand() limit 1", nativeQuery = true)
    Goods findRandomGoodsWithLowRatingCount(@Param("targetUser") User user);

    @Query(value = "select g1.* " +
            "from goods g1 " +
            "where g1.goods_id not in " +
            "(select g2.goods_id " +
            "from goods g2 " +
            "inner join rating_goods rg on rg.goods_id = g2.goods_id " +
            "inner join rating r on r.rating_goods_id = rg.rating_goods_id " +
            "inner join user_rating_relation urr on urr.rating_id = r.rating_id " +
            "inner join user u on u.user_id = urr.user_id " +
            "where u.user_id = :#{#targetUser.userId}) " +
            "and g1.user_id <> :#{#targetUser.userId} " +
            "and g1.is_deleted = false " +
            "order by rand() limit 1", nativeQuery = true)
    Goods findRandomGoods(@Param("targetUser") User user);

    Optional<Goods> findByGoodsId(Long goodsId);


    @Query("SELECT g from Goods g ORDER BY g.createdAt desc limit 8")
    List<Goods> findTop8ByCreatedAt();
    List<Goods> findAllByUserAndIsDeletedFalseAndGoodsStatus(User user, GoodsStatus onsale);
}
