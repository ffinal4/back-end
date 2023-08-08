package com.example.peeppo.domain.goods.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWishlist is a Querydsl query type for Wishlist
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWishlist extends EntityPathBase<Wishlist> {

    private static final long serialVersionUID = 1656898572L;

    public static final QWishlist wishlist = new QWishlist("wishlist");

    public final NumberPath<Long> wishlistId = createNumber("wishlistId", Long.class);

    public QWishlist(String variable) {
        super(Wishlist.class, forVariable(variable));
    }

    public QWishlist(Path<? extends Wishlist> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWishlist(PathMetadata metadata) {
        super(Wishlist.class, metadata);
    }

}

