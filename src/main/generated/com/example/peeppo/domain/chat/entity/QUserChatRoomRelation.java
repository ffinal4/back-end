package com.example.peeppo.domain.chat.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserChatRoomRelation is a Querydsl query type for UserChatRoomRelation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserChatRoomRelation extends EntityPathBase<UserChatRoomRelation> {

    private static final long serialVersionUID = -1356196485L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserChatRoomRelation userChatRoomRelation = new QUserChatRoomRelation("userChatRoomRelation");

    public final com.example.peeppo.domain.user.entity.QUser buyer;

    public final QChatRoom chatRoom;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.peeppo.domain.user.entity.QUser seller;

    public QUserChatRoomRelation(String variable) {
        this(UserChatRoomRelation.class, forVariable(variable), INITS);
    }

    public QUserChatRoomRelation(Path<? extends UserChatRoomRelation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserChatRoomRelation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserChatRoomRelation(PathMetadata metadata, PathInits inits) {
        this(UserChatRoomRelation.class, metadata, inits);
    }

    public QUserChatRoomRelation(Class<? extends UserChatRoomRelation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.buyer = inits.isInitialized("buyer") ? new com.example.peeppo.domain.user.entity.QUser(forProperty("buyer")) : null;
        this.chatRoom = inits.isInitialized("chatRoom") ? new QChatRoom(forProperty("chatRoom"), inits.get("chatRoom")) : null;
        this.seller = inits.isInitialized("seller") ? new com.example.peeppo.domain.user.entity.QUser(forProperty("seller")) : null;
    }

}

