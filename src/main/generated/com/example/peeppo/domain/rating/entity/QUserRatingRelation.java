package com.example.peeppo.domain.rating.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserRatingRelation is a Querydsl query type for UserRatingRelation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserRatingRelation extends EntityPathBase<UserRatingRelation> {

    private static final long serialVersionUID = -1825649974L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserRatingRelation userRatingRelation = new QUserRatingRelation("userRatingRelation");

    public final QRating rating;

    public final com.example.peeppo.domain.user.entity.QUser user;

    public final NumberPath<Long> userRatingRelationId = createNumber("userRatingRelationId", Long.class);

    public QUserRatingRelation(String variable) {
        this(UserRatingRelation.class, forVariable(variable), INITS);
    }

    public QUserRatingRelation(Path<? extends UserRatingRelation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserRatingRelation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserRatingRelation(PathMetadata metadata, PathInits inits) {
        this(UserRatingRelation.class, metadata, inits);
    }

    public QUserRatingRelation(Class<? extends UserRatingRelation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.rating = inits.isInitialized("rating") ? new QRating(forProperty("rating"), inits.get("rating")) : null;
        this.user = inits.isInitialized("user") ? new com.example.peeppo.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

