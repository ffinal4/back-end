package com.example.peeppo.domain.goods.service;

import com.amazonaws.services.s3.AmazonS3;
import com.example.peeppo.domain.goods.dto.DeleteResponseDto;
import com.example.peeppo.domain.goods.dto.GoodsRequestDto;
import com.example.peeppo.domain.goods.dto.GoodsResponseDto;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.repository.GoodsRepository;
import com.example.peeppo.domain.image.repository.ImageRepository;
import com.example.peeppo.domain.image.entity.Image;
import com.example.peeppo.domain.image.utils.ImageUtil;
import com.example.peeppo.global.responseDto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoodsService {
    private final GoodsRepository goodsRepository;
    private final ImageRepository imageRepository;
    private final ImageUtil imageUtil;
    private final AmazonS3 amazonS3;
    private final String bucket;


    @Transactional
    public ApiResponse<GoodsResponseDto> goodsCreate(GoodsRequestDto requestDto) {
        List<MultipartFile> images = requestDto.getImages();

        images.stream().filter(image -> !imageUtil.validateFile(image)).findFirst().ifPresent(image -> {
            throw new IllegalStateException("파일 검증 실패");
        });
        //S3에 업로드 후 이미지 키 반환.
        List<String> imageUuids = imageUtil.uploadFileToS3(images, amazonS3, bucket);
        List<Image> S3ObjectUrl = new ArrayList<>();
        Goods goods = goodsRepository.save(new Goods(requestDto));
        for (String imageUuid : imageUuids) {
            Image image = new Image(imageUuid, amazonS3.getUrl(bucket, imageUuid).toString(), goods);
            S3ObjectUrl.add(image);
            imageRepository.save(image);
        }

        GoodsResponseDto responseDto = new GoodsResponseDto(goods, S3ObjectUrl);
        return new ApiResponse<>(true, responseDto, null);
    }

    // image, username, title, content,
    public ApiResponse<List<GoodsResponseDto>> allGoods() {
        List<Goods> goodsList = goodsRepository.findAllByIsDeletedFalseOrderByGoodsIdDesc();
        List<GoodsResponseDto> goodsResponseList = new ArrayList<>();

        for (Goods goods : goodsList) {
            List<Image> images = imageRepository.findByGoodsGoodsId(goods.getGoodsId());
            GoodsResponseDto goodsResponseDto = new GoodsResponseDto(goods, images);
            goodsResponseList.add(goodsResponseDto);
        }

        return new ApiResponse<>(true, goodsResponseList, null);

    }

//    public ApiResponse<List<GoodsResponseDto>> locationAllGoods(Long locationId) {
//        List<Goods> goodsList = goodsRepository.findAllByLocationIdAndIsDeletedFalseOrderByGoodsIdDesc(locationId);
//        List<GoodsResponseDto> goodsResponseList = responseDtoList(goodsList);
//
//        return new ApiResponse<>(true, goodsResponseList, null);
//    }


    public ApiResponse<GoodsResponseDto> getGoods(Long goodsId) {
        Goods goods = findGoods(goodsId);
        List<Image> S3ObjectUrl = imageRepository.findByGoodsGoodsId(goodsId);

        deleteCheck(goods);
        GoodsResponseDto goodsResponseDto = new GoodsResponseDto(goods, S3ObjectUrl);

        return new ApiResponse<>(true, goodsResponseDto, null);

    }

    @Transactional
    public ApiResponse<GoodsResponseDto> goodsUpdate(Long goodsId, GoodsRequestDto requestDto) {
        Goods goods = findGoods(goodsId);
        deleteCheck(goods);

        List<Image> imagesToDelete = imageRepository.findByGoodsGoodsId(goodsId);

        // 이미지 삭제
        for (Image image : imagesToDelete) {
            imageUtil.deleteFileFromS3(image.getImageKey(), amazonS3, bucket);
        }
        imageRepository.deleteByGoodsGoodsId(goodsId);

        // 이미지 등록
        List<MultipartFile> images = requestDto.getImages();

        images.stream().filter(image -> !imageUtil.validateFile(image)).findFirst().ifPresent(image -> {
            throw new IllegalStateException("파일 검증 실패");
        });

        //S3에 업로드 후 이미지 키 반환.
        List<String> imageUuids = imageUtil.uploadFileToS3(images, amazonS3, bucket);
        List<Image> S3ObjectUrl = new ArrayList<>();
        for (String imageUuid : imageUuids) {
            Image image = new Image(imageUuid, amazonS3.getUrl(bucket, imageUuid).toString(), goods);
            S3ObjectUrl.add(image);
            imageRepository.save(image);
        }

        // goods 테이블 수정
        goods.update(requestDto);
        GoodsResponseDto responseDto = new GoodsResponseDto(goods, S3ObjectUrl);

        return new ApiResponse<>(true, responseDto, null);

    }

    @Transactional
    public ApiResponse<DeleteResponseDto> deleteGoods(Long goodsId) {
        Goods goods = findGoods(goodsId);
        goods.setDeleted(true);
        goodsRepository.save(goods);

        return new ApiResponse<>(true, new DeleteResponseDto("삭제되었습니다"), null);
    }


    public List<GoodsResponseDto> responseDtoList(List<Goods> goodsList) {
        return goodsList.stream()
                .map(GoodsResponseDto::new)
                .collect(Collectors.toList());
    }

    public Goods findGoods(Long goodsId) {
        return goodsRepository.findById(goodsId).orElseThrow(() ->
                new NullPointerException("해당 게시글은 존재하지 않습니다."));
    }

    public void deleteCheck(Goods goods) {
        if (goods.isDeleted()) {
            throw new IllegalStateException("삭제된 게시글입니다.");
        }
    }

}
