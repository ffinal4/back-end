package com.example.peeppo.domain.bid.helper;

import com.example.peeppo.domain.bid.entity.Bid;
import com.example.peeppo.domain.bid.enums.BidStatus;
import com.example.peeppo.domain.bid.repository.BidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BidHelper {
    private final BidRepository bidRepository;

    @Transactional
    public void bidTradeCompleted(List<Long> requestIds, Long userId) {

        List<Bid> bidList = bidRepository.findAllById(requestIds);

        for (Bid bid : bidList) {
            if (!Objects.equals(userId, bid.getUser().getUserId())) {
                throw new IllegalArgumentException("자신의 물건이 아닌 물품이 존재합니다");
            }
//                if (!bid.getBidStatus().equals(BidStatus.SUCCESS)) {
//                    throw new IllegalArgumentException("정상적인 접근이 아닙니다.");
//                }
            bid.changeBidStatus(BidStatus.DONE);
//                bidList.add(bid);
        }
        bidRepository.saveAll(bidList);
    }
}

