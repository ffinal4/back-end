package com.example.peeppo.domain.rating.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRating is a Querydsl query type for Rating
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRating extends EntityPathBase<Rating> {

    private static final long serialVersionUID = 557124099L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRating rating = new QRating("rating");

    public final NumberPath<Long> avgRatingPrice = createNumber("avgRatingPrice", Long.class);

    public final com.example.peeppo.domain.goods.entity.QGoods goods;

    public final com.example.peeppo.domain.image.entity.QImage image;

    public final NumberPath<Long> ratingCount = createNumber("ratingCount", Long.class);

    public final NumberPath<Long> ratingId = createNumber("ratingId", Long.class);

    public final NumberPath<Long> sellerPrice = createNumber("sellerPrice", Long.class);

    public final NumberPath<Long> sumRatingPrice = createNumber("sumRatingPrice", Long.class);

    public QRating(String variable) {
        this(Rating.class, forVariable(variable), INITS);
    }

    public QRating(Path<? extends Rating> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRating(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRating(PathMetadata metadata, PathInits inits) {
        this(Rating.class, metadata, inits);
    }

    public QRating(Class<? extends Rating> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.goods = inits.isInitialized("goods") ? new com.example.peeppo.domain.goods.entity.QGoods(forProperty("goods"), inits.get("goods")) : null;
        this.image = inits.isInitialized("image") ? new com.example.peeppo.domain.image.entity.QImage(forProperty("image"), inits.get("image")) : null;
    }

}

