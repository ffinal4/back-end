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

    public ImageRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Image> findByGoodsGoodsIdOrderByCreatedAtAsc(Long goodsId) {
        QImage qImage = new QImage("qImage");
        return queryFactory
                .selectFrom(qImage)
                .where(qImage.goods.goodsId.eq(goodsId))
                .orderBy(qImage.createdAt.asc())
                .fetch();
    }

    @Override
    public Image findByGoodsGoodsIdOrderByCreatedAtAscFirst(Long goodsId) {
        QImage qImage = new QImage("qImage");
        return queryFactory
                .selectFrom(qImage)
                .where(qImage.goods.goodsId.eq(goodsId))
                .orderBy(qImage.createdAt.asc())
                .fetchOne();
    }
}
