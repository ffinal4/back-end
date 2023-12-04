package com.example.peeppo.domain.dibs.service;

import com.example.peeppo.domain.dibs.dto.DibsRequestDto;
import com.example.peeppo.domain.dibs.entity.Dibs;
import com.example.peeppo.domain.dibs.repository.DibsRepository;
import com.example.peeppo.domain.goods.dto.GoodsListResponseDto;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.repository.goods.GoodsRepository;
import com.example.peeppo.domain.image.repository.ImageRepository;
import com.example.peeppo.domain.user.dto.CheckResponseDto;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.repository.UserRepository;
import com.example.peeppo.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
public class DibsService {

    private final UserRepository userRepository;
    private final GoodsRepository goodsRepository;
    private final DibsRepository dibsRepository;
    private final ImageRepository imageRepository;

    @Transactional(readOnly = true)
    public boolean checkDibsGoods(Long userId, Long GoodsId) {
        return dibsRepository.findByUserUserIdAndGoodsGoodsIdAndGoodsIsDeletedFalse(userId, GoodsId).isPresent();
    }

    @Transactional
    public CheckResponseDto dibsGoods(UserDetailsImpl userDetails, DibsRequestDto dibsRequestDto) {
        User user = userDetails.getUser();
        Goods goods = findGoods(dibsRequestDto.getGoodsId());

        Optional<Dibs> dibsGoods = dibsRepository.findByUserUserIdAndGoodsGoodsIdAndGoodsIsDeletedFalse(user.getUserId(), goods.getGoodsId());

        // 찜한 경우
        if (dibsGoods.isPresent()) {
            dibsRepository.delete(dibsGoods.get());
        }

        dibsRepository.save(new Dibs(goods, user));

        return new CheckResponseDto("완료", dibsGoods.isPresent(), OK.value(), "OK");
}

    @Transactional(readOnly = true)
    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 userId 입니다."));
    }

    @Transactional(readOnly = true)
    public Goods findGoods(Long goodsId) {
        return goodsRepository.findById(goodsId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 goodsId 입니다."));
    }

    @Transactional(readOnly = true)
    public List<GoodsListResponseDto> getDibsGoods(User user) {
        List<Dibs> dibsList = dibsRepository.findByUserUserIdAndGoodsIsDeletedFalse(user.getUserId());
        List<GoodsListResponseDto> goodsListResponseDtos = new ArrayList<>();
        for (Dibs dibs1 : dibsList) {
            boolean checkDibs = checkDibsGoods(user.getUserId(), dibs1.getGoods().getGoodsId());
            String imageUrl = imageRepository.findByGoodsGoodsIdOrderByCreatedAtAscFirst(dibs1.getGoods().getGoodsId()).getImageUrl();
            goodsListResponseDtos.add(new GoodsListResponseDto(dibs1.getGoods(), checkDibs, imageUrl));
        }
        return goodsListResponseDtos;
    }
}
