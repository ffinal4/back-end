package com.example.peeppo.domain.image.repository;

import com.example.peeppo.domain.image.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    UserImage findByUserUserId(Long userId);
}
