package com.example.peeppo.domain.goods.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRequestGoods is a Querydsl query type for RequestGoods
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRequestGoods extends EntityPathBase<RequestGoods> {

    private static final long serialVersionUID = 1321863374L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRequestGoods requestGoods = new QRequestGoods("requestGoods");

    public final com.example.peeppo.global.utils.QTimestamped _super = new com.example.peeppo.global.utils.QTimestamped(this);

    public final QGoods buyer;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<Long> receiveUser = createNumber("receiveUser", Long.class);

    public final NumberPath<Long> requestId = createNumber("requestId", Long.class);

    public final EnumPath<com.example.peeppo.domain.goods.enums.RequestStatus> requestStatus = createEnum("requestStatus", com.example.peeppo.domain.goods.enums.RequestStatus.class);

    public final QGoods seller;

    public final com.example.peeppo.domain.user.entity.QUser user;

    public QRequestGoods(String variable) {
        this(RequestGoods.class, forVariable(variable), INITS);
    }

    public QRequestGoods(Path<? extends RequestGoods> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRequestGoods(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRequestGoods(PathMetadata metadata, PathInits inits) {
        this(RequestGoods.class, metadata, inits);
    }

    public QRequestGoods(Class<? extends RequestGoods> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.buyer = inits.isInitialized("buyer") ? new QGoods(forProperty("buyer"), inits.get("buyer")) : null;
        this.seller = inits.isInitialized("seller") ? new QGoods(forProperty("seller"), inits.get("seller")) : null;
        this.user = inits.isInitialized("user") ? new com.example.peeppo.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

