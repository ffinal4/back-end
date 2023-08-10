package com.example.peeppo.domain.rating.entity;

import com.example.peeppo.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserRatingRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userRatingRelationId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "rating_id", nullable = false)
    @JsonBackReference
    private Rating rating;

    public UserRatingRelation(User user) {
        this.user = user;
    }

    public UserRatingRelation(Rating rating) {
        this.rating = rating;
    }

    public UserRatingRelation(User user, Rating rating) {
        this.user = user;
        this.rating = rating;
    }
}
