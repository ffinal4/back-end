package com.example.peeppo.domain.rating.repository.userRatingRepository;

import java.util.Set;

public interface UserRatingRepositoryCustom {
    Set<Long> findUserCheckedGoodsByUserId(Long userId);
}
