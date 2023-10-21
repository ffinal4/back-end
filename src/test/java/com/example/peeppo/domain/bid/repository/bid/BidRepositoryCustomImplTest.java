package com.example.peeppo.domain.bid.repository.bid;

import com.example.peeppo.domain.bid.entity.Bid;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("security")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BidRepositoryCustomImplTest {

    @Autowired
    private BidRepository bidRepository;

    @Test
    void findSortedBySellersPickTest() {
        Long auctionId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 20);

        Page<Bid> result = bidRepository.findSortedBySellersPick(auctionId, pageRequest);
        assertThat(result).isNotNull();
    }

}