package com.example.peeppo.domain.bid.dto;

import com.example.peeppo.domain.bid.entity.Bid;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BidResponseListDto {
    private List<Long> bidId;
    private Long userId;
    private String image;
    private String title;
    private String location;
    private Long bidCount;
    private boolean sellersPick;


    public BidResponseListDto(List<Long> bidList, Bid bid, String imageUrl, Long bidCount) {
        this.bidId = bidList;
        this.userId = bid.getUser().getUserId();
        this.image = imageUrl;
        this.title = bid.getTitle();
        this.location = bid.getLocation();
        this.bidCount = bidCount;
        this.sellersPick = bid.isSellersPick();
    }
}
