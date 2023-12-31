package com.example.peeppo.domain.image.repository;

import com.example.peeppo.domain.image.entity.Image;
import com.example.peeppo.domain.image.entity.QImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import java.util.List;

@Repository
public class ImageRepositoryCustomImpl implements ImageRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QImage qImage;
    public ImageRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.qImage = new QImage("qImage");
    }

    @Override
    public List<Image> findByGoodsGoodsIdOrderByCreatedAtAsc(Long goodsId) {
        return queryFactory
                .selectFrom(qImage)
                .where(qImage.goods.goodsId.eq(goodsId))
                .orderBy(qImage.createdAt.asc())
                .fetch();
    }

    @Override
    public Image findByGoodsGoodsIdOrderByCreatedAtAscFirst(Long goodsId) {
        return queryFactory
                .selectFrom(qImage)
                .where(qImage.goods.goodsId.eq(goodsId))
                .orderBy(qImage.createdAt.asc())
                .limit(1)
                .fetchOne();
    }
}
