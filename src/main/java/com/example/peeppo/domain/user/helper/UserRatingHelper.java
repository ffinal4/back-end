package com.example.peeppo.domain.user.helper;

import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserRatingHelper {
    private final UserRepository userRepository;

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void userRatingCountUpdate(User user, Long count, Long point) {
        user.countUpdate(count, point);
        userRepository.save(user);
    }

}
