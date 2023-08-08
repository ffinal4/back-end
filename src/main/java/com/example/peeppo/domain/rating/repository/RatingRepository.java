package com.example.peeppo.domain.rating.repository;

import com.example.peeppo.domain.rating.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {

}
