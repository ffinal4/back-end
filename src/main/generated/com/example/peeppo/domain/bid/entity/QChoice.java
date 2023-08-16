package com.example.peeppo.domain.bid.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChoice is a Querydsl query type for Choice
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChoice extends EntityPathBase<Choice> {

    private static final long serialVersionUID = -1580778481L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChoice choice = new QChoice("choice");

    public final com.example.peeppo.domain.auction.entity.QAuction auction;

    public final QBid bid;

    public final NumberPath<Long> choiceId = createNumber("choiceId", Long.class);

    public final StringPath goodsImg = createString("goodsImg");

    public final StringPath location = createString("location");

    public final StringPath title = createString("title");

    public QChoice(String variable) {
        this(Choice.class, forVariable(variable), INITS);
    }

    public QChoice(Path<? extends Choice> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChoice(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChoice(PathMetadata metadata, PathInits inits) {
        this(Choice.class, metadata, inits);
    }

    public QChoice(Class<? extends Choice> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.auction = inits.isInitialized("auction") ? new com.example.peeppo.domain.auction.entity.QAuction(forProperty("auction"), inits.get("auction")) : null;
        this.bid = inits.isInitialized("bid") ? new QBid(forProperty("bid"), inits.get("bid")) : null;
    }

}

