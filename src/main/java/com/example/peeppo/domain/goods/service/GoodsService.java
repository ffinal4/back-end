package com.example.peeppo.domain.goods.service;

import com.example.peeppo.domain.goods.dto.GoodsResponseDto;
import com.example.peeppo.domain.goods.dto.goodsRequestDto;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.repository.GoodsRepository;
import com.example.peeppo.global.responseDto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoodsService {

    private final GoodsRepository goodsRepository;

    public ApiResponse<GoodsResponseDto> goodsCreate(goodsRequestDto reqeustDto) {

        Goods goods = goodsRepository.save(new Goods(reqeustDto));
        GoodsResponseDto responseDto = new GoodsResponseDto(goods);
        return new ApiResponse<>(true, responseDto, null);
    }

    public ApiResponse<List<GoodsResponseDto>> allGoods() {
        List<Goods> goodsList = goodsRepository.findAllByOrderByCreatedAtDesc();
        List<GoodsResponseDto> goodsResponseList = responseDtoList(goodsList);
        return new ApiResponse<>(true, goodsResponseList, null);

    }

    public ApiResponse<List<GoodsResponseDto>> locationAllGoods(Long locationId) {
        List<Goods> goodsList = goodsRepository.findAllByLocationIdOrderByCreatedAtDesc(locationId);
        List<GoodsResponseDto> goodsResponseList = responseDtoList(goodsList);

        return new ApiResponse<>(true, goodsResponseList, null);
    }

    public List<GoodsResponseDto> responseDtoList(List<Goods> goodsList) {
        return goodsList.stream()
                .map(GoodsResponseDto::new) // 또는 GoodsResponseDto의 생성자를 호출하여 변환
                .collect(Collectors.toList());
    }
}
