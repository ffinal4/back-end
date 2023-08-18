package com.example.peeppo.domain.rating.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRatingGoods is a Querydsl query type for RatingGoods
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRatingGoods extends EntityPathBase<RatingGoods> {

    private static final long serialVersionUID = 1906075411L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRatingGoods ratingGoods = new QRatingGoods("ratingGoods");

    public final NumberPath<Double> avgRatingPrice = createNumber("avgRatingPrice", Double.class);

    public final com.example.peeppo.domain.goods.entity.QGoods goods;

    public final NumberPath<Double> nextAvgRatingPrice = createNumber("nextAvgRatingPrice", Double.class);

    public final NumberPath<Long> ratingCount = createNumber("ratingCount", Long.class);

    public final NumberPath<Long> ratingGoodsId = createNumber("ratingGoodsId", Long.class);

    public final NumberPath<Long> sumRatingPrice = createNumber("sumRatingPrice", Long.class);

    public final NumberPath<Long> version = createNumber("version", Long.class);

    public QRatingGoods(String variable) {
        this(RatingGoods.class, forVariable(variable), INITS);
    }

    public QRatingGoods(Path<? extends RatingGoods> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRatingGoods(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRatingGoods(PathMetadata metadata, PathInits inits) {
        this(RatingGoods.class, metadata, inits);
    }

    public QRatingGoods(Class<? extends RatingGoods> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.goods = inits.isInitialized("goods") ? new com.example.peeppo.domain.goods.entity.QGoods(forProperty("goods"), inits.get("goods")) : null;
    }

}

