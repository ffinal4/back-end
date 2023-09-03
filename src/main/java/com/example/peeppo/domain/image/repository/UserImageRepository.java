package com.example.peeppo.domain.image.repository;

import com.example.peeppo.domain.image.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    Optional<UserImage> findByUserUserId(Long userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from UserImage ui where  ui.imageKey = :imageKey")
    void deleteUserImageByImageKeyQuery(@Param("imageKey") String imageKey);

}
