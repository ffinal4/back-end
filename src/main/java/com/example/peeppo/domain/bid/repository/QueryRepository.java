package com.example.peeppo.domain.bid.repository;

import com.example.peeppo.domain.bid.entity.Bid;
import com.example.peeppo.domain.bid.entity.Choice;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryRepository {
    private final EntityManager entityManager;

    public List<Bid> findBid(Long userId, Long auctionId) {
        String query = "select b from Bid b where b.user.id = :userId and b.auction.id = :auctionId";

        try {
            return entityManager.createQuery(query, Bid.class)
                    .setParameter("userId", userId)
                    .setParameter("auctionId", auctionId)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    public List<Choice> findChoice(Long auctionId) {
        String query = "select c from Choice c where c.auction.id = :auctionId";

        try {
            return entityManager.createQuery(query, Choice.class)
                    .setParameter("auctionId", auctionId)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }
}
