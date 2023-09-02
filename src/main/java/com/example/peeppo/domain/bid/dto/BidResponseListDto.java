package com.example.peeppo.domain.bid.dto;

import com.example.peeppo.domain.bid.entity.Bid;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BidResponseListDto {
    private String image;
    private String title;
    private String location;
    private Long bidCount;
    private boolean sellersPick;


    public BidResponseListDto(Bid bid, String imageUrl, Long bidCount){
        this.image = imageUrl;
        this.title = bid.getTitle();
        this.location = bid.getLocation();
        this.bidCount = bidCount;
        this.sellersPick = bid.isSellersPick();
    }
}
