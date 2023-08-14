package com.example.peeppo.domain.user.helper;

import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserHelper {
    private final UserRepository userRepository;

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }


    @Transactional
    public void userRatingCheck(User user, Long ratingCount, Long userPoint){
        user.maxCountUpdate(ratingCount, userPoint);
    }

}
