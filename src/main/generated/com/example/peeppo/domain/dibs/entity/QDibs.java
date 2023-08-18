package com.example.peeppo.domain.dibs.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDibs is a Querydsl query type for Dibs
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDibs extends EntityPathBase<Dibs> {

    private static final long serialVersionUID = -431956747L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDibs dibs = new QDibs("dibs");

    public final NumberPath<Long> dibsId = createNumber("dibsId", Long.class);

    public final com.example.peeppo.domain.goods.entity.QGoods goods;

    public final com.example.peeppo.domain.user.entity.QUser user;

    public QDibs(String variable) {
        this(Dibs.class, forVariable(variable), INITS);
    }

    public QDibs(Path<? extends Dibs> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDibs(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDibs(PathMetadata metadata, PathInits inits) {
        this(Dibs.class, metadata, inits);
    }

    public QDibs(Class<? extends Dibs> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.goods = inits.isInitialized("goods") ? new com.example.peeppo.domain.goods.entity.QGoods(forProperty("goods"), inits.get("goods")) : null;
        this.user = inits.isInitialized("user") ? new com.example.peeppo.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

