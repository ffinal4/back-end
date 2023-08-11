package com.example.peeppo.domain.rating.repository.userRatingRepository;

import com.example.peeppo.domain.rating.entity.UserRatingRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRatingRelationRepository extends JpaRepository<UserRatingRelation, Long>, UserRatingRepositoryCustom {


}
