package com.example.peeppo.domain.goods.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGoods is a Querydsl query type for Goods
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGoods extends EntityPathBase<Goods> {

    private static final long serialVersionUID = 211945071L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGoods goods = new QGoods("goods");

    public final com.example.peeppo.global.utils.QTimestamped _super = new com.example.peeppo.global.utils.QTimestamped(this);

    public final com.example.peeppo.domain.auction.entity.QAuction auction;

    public final EnumPath<com.example.peeppo.domain.goods.enums.Category> category = createEnum("category", com.example.peeppo.domain.goods.enums.Category.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath goodsCondition = createString("goodsCondition");

    public final NumberPath<Long> goodsId = createNumber("goodsId", Long.class);

    public final EnumPath<com.example.peeppo.domain.bid.enums.GoodsStatus> goodsStatus = createEnum("goodsStatus", com.example.peeppo.domain.bid.enums.GoodsStatus.class);

    public final ListPath<com.example.peeppo.domain.image.entity.Image, com.example.peeppo.domain.image.entity.QImage> image = this.<com.example.peeppo.domain.image.entity.Image, com.example.peeppo.domain.image.entity.QImage>createList("image", com.example.peeppo.domain.image.entity.Image.class, com.example.peeppo.domain.image.entity.QImage.class, PathInits.DIRECT2);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath location = createString("location");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<Long> sellerPrice = createNumber("sellerPrice", Long.class);

    public final StringPath title = createString("title");

    public final StringPath tradeType = createString("tradeType");

    public final com.example.peeppo.domain.user.entity.QUser user;

    public final QWantedGoods wantedGoods;

    public QGoods(String variable) {
        this(Goods.class, forVariable(variable), INITS);
    }

    public QGoods(Path<? extends Goods> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGoods(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGoods(PathMetadata metadata, PathInits inits) {
        this(Goods.class, metadata, inits);
    }

    public QGoods(Class<? extends Goods> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.auction = inits.isInitialized("auction") ? new com.example.peeppo.domain.auction.entity.QAuction(forProperty("auction"), inits.get("auction")) : null;
        this.user = inits.isInitialized("user") ? new com.example.peeppo.domain.user.entity.QUser(forProperty("user")) : null;
        this.wantedGoods = inits.isInitialized("wantedGoods") ? new QWantedGoods(forProperty("wantedGoods")) : null;
    }

}

