package com.example.peeppo.domain.user.repository;

import com.example.peeppo.domain.user.entity.User;

import java.util.List;

public interface UserRepositoryCustom {

    List<User> findTopThreeUsersByMaxRatingCount();
    List<User> findTopFiveUsersByMaxRatingCount();
}
