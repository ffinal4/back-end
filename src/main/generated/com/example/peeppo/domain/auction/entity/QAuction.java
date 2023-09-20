package com.example.peeppo.domain.auction.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAuction is a Querydsl query type for Auction
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuction extends EntityPathBase<Auction> {

    private static final long serialVersionUID = -922263153L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAuction auction = new QAuction("auction");

    public final com.example.peeppo.global.utils.QTimestamped _super = new com.example.peeppo.global.utils.QTimestamped(this);

    public final DateTimePath<java.time.LocalDateTime> auctionEndTime = createDateTime("auctionEndTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> auctionId = createNumber("auctionId", Long.class);

    public final EnumPath<com.example.peeppo.domain.auction.enums.AuctionStatus> auctionStatus = createEnum("auctionStatus", com.example.peeppo.domain.auction.enums.AuctionStatus.class);

    public final NumberPath<Double> avgPrice = createNumber("avgPrice", Double.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.example.peeppo.domain.goods.entity.QGoods goods;

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final NumberPath<Double> lowPrice = createNumber("lowPrice", Double.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.example.peeppo.domain.user.entity.QUser user;

    public QAuction(String variable) {
        this(Auction.class, forVariable(variable), INITS);
    }

    public QAuction(Path<? extends Auction> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAuction(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAuction(PathMetadata metadata, PathInits inits) {
        this(Auction.class, metadata, inits);
    }

    public QAuction(Class<? extends Auction> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.goods = inits.isInitialized("goods") ? new com.example.peeppo.domain.goods.entity.QGoods(forProperty("goods"), inits.get("goods")) : null;
        this.user = inits.isInitialized("user") ? new com.example.peeppo.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

