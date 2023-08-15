package com.example.peeppo.domain.rating.repository.ratingRepository;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.rating.entity.Rating;
import com.example.peeppo.domain.user.entity.User;

import java.util.List;
import java.util.Set;

public interface RatingRepositoryCustom {

    Boolean existRatingByUserAndGoods(User targetUser, Goods targetGoods);
}
