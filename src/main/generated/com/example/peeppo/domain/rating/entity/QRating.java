package com.example.peeppo.domain.rating.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRating is a Querydsl query type for Rating
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRating extends EntityPathBase<Rating> {

    private static final long serialVersionUID = 557124099L;

    public static final QRating rating = new QRating("rating");

    public final NumberPath<Long> avgRatingPrice = createNumber("avgRatingPrice", Long.class);

    public final NumberPath<Long> ratingCount = createNumber("ratingCount", Long.class);

    public final NumberPath<Long> ratingId = createNumber("ratingId", Long.class);

    public final NumberPath<Long> sellerPrice = createNumber("sellerPrice", Long.class);

    public final NumberPath<Long> sumRatingPrice = createNumber("sumRatingPrice", Long.class);

    public QRating(String variable) {
        super(Rating.class, forVariable(variable));
    }

    public QRating(Path<? extends Rating> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRating(PathMetadata metadata) {
        super(Rating.class, metadata);
    }

}

