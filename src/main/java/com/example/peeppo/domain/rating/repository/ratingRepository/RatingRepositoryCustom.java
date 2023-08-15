package com.example.peeppo.domain.rating.repository.ratingRepository;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.user.entity.User;

public interface RatingRepositoryCustom {

    Boolean existRatingByUserAndGoods(User targetUser, Goods targetGoods);
}
