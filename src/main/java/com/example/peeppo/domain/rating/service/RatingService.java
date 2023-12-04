package com.example.peeppo.domain.rating.service;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.repository.goods.GoodsRepository;
import com.example.peeppo.domain.image.entity.UserImage;
import com.example.peeppo.domain.image.repository.UserImageRepository;
import com.example.peeppo.domain.rating.dto.RatingRequestDto;
import com.example.peeppo.domain.rating.dto.RatingResponseDto;
import com.example.peeppo.domain.rating.dto.RatingScoreResponseDto;
import com.example.peeppo.domain.rating.helper.RatingHelper;
import com.example.peeppo.domain.rating.repository.ratingRepository.RatingRepository;
import com.example.peeppo.domain.user.dto.RatingUserResponseDto;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.repository.UserRepository;
import com.example.peeppo.global.responseDto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RatingService {
    private final UserImageRepository userImageRepository;

    private final RatingRepository ratingRepository;
    private final GoodsRepository goodsRepository;
    private final UserRepository userRepository;
    private final RatingHelper ratingHelper;

    @Transactional(readOnly = true)
    public ApiResponse<List<RatingUserResponseDto>> ratingTopFiveUsers() {
        List<RatingUserResponseDto> responseDtoList = userRepository.findTopUsersByMaxRatingCount(5)
                .stream()
                .map(user -> {
                    UserImage userImage = userImageRepository.findByUserUserId(user.getUserId())
                            .orElse(null);
                    String imageUrl = (userImage != null) ? userImage.getImageUrl() : null;
                    return new RatingUserResponseDto(imageUrl, user.getNickname(), user.getMaxRatingCount());
                })
                .collect(Collectors.toList());

        return new ApiResponse<>(true, responseDtoList, null);
    }

    @Transactional
    public ApiResponse<RatingResponseDto> randomRatingGoods(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 유저입니다."));

        if (user.getCurrentRatingCount() == 0L){
            user.totalPointInit();
        }

        Goods randomGoods = goodsRepository.findRandomGoodsWithLowRatingCount(user)
                .orElse(goodsRepository.findRandomGoods(user)
                        .orElseThrow(() -> new NullPointerException("평가 가능한 상품이 존재하지 않습니다.")));

        RatingResponseDto ratingResponseDto = new RatingResponseDto(randomGoods, user.getCurrentRatingCount());

        return new ApiResponse<>(true, ratingResponseDto, null);
    }

    @Transactional
    public ApiResponse<RatingScoreResponseDto> randomRatingGoods(RatingRequestDto ratingRequestDto,
                                                                 Long userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 유저입니다."));
        Goods goods = goodsRepository.findByGoodsId(ratingRequestDto.getGoodsId())
                .orElseThrow(() -> new NullPointerException("해당 상품이 존재하지 않습니다."));

        if (ratingRepository.existRatingByUserAndGoods(user, goods)) {
            throw new IllegalStateException("이미 해당 상품에 대한 평가가 존재합니다.");
        }
        if (goods.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalStateException("자신의 게시글은 평가할 수 없습니다.");
        }

        // 값 측정이 올바른지 확인 후 포인트 가산
        List<Long> currentPointAndCurrentCount = ratingHelper
                .isCorrectedAndUpdateGoodsRating(user, goods, ratingRequestDto.getRatingPrice());
        Long currentPoint = currentPointAndCurrentCount.get(0);
        Long currentCount = currentPointAndCurrentCount.get(1);
        RatingScoreResponseDto responseDto = new RatingScoreResponseDto(
                goods.getGoodsId(),
                currentPoint,
                currentCount,
                ratingRequestDto.getRatingPrice(),
                goods.getSellerPrice(),
                user.getTotalPoint());
        return new ApiResponse<>(true, responseDto, null);
    }

}
