package com.example.peeppo.domain.goods.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWantedGoods is a Querydsl query type for WantedGoods
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWantedGoods extends EntityPathBase<WantedGoods> {

    private static final long serialVersionUID = -2036819392L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWantedGoods wantedGoods = new QWantedGoods("wantedGoods");

    public final EnumPath<com.example.peeppo.domain.goods.enums.Category> category = createEnum("category", com.example.peeppo.domain.goods.enums.Category.class);

    public final StringPath content = createString("content");

    public final QGoods goods;

    public final StringPath title = createString("title");

    public final NumberPath<Long> wantedId = createNumber("wantedId", Long.class);

    public QWantedGoods(String variable) {
        this(WantedGoods.class, forVariable(variable), INITS);
    }

    public QWantedGoods(Path<? extends WantedGoods> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWantedGoods(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWantedGoods(PathMetadata metadata, PathInits inits) {
        this(WantedGoods.class, metadata, inits);
    }

    public QWantedGoods(Class<? extends WantedGoods> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.goods = inits.isInitialized("goods") ? new QGoods(forProperty("goods"), inits.get("goods")) : null;
    }

}

