package com.example.peeppo.domain.user.helper;

import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.helper.repository.UserRepository;
import com.example.peeppo.global.exception.CustomTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserRatingHelper {
    private final UserRepository userRepository;

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomTokenException("해당 accessToken은 만료되었습니다."));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void userRatingCountUpdate(User user, Long count, Long point) {
        user.countUpdate(count, point);
        userRepository.save(user);
    }
}
