package com.example.peeppo.domain.bid.repository;

import com.example.peeppo.domain.bid.entity.Choice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChoiceBidRepository extends JpaRepository<Choice, Long> {
}
