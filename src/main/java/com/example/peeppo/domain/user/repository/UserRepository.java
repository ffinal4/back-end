package com.example.peeppo.domain.user.repository;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByUserId(Long userId);

    User findUserByNickname(String nickname);

    @Query("SELECT DISTINCT b.auction FROM Bid b WHERE b.user.userId = :user_id")
    Page<User> findAuctionListByUserUserId(@Param("user_id") Long userId, Pageable pageable);
}
