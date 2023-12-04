package com.example.peeppo.domain.goods;

import com.example.peeppo.domain.goods.dto.GoodsRequestDto;
import com.example.peeppo.domain.goods.dto.MsgResponseDto;
import com.example.peeppo.domain.goods.dto.WantedRequestDto;
import com.example.peeppo.domain.goods.service.GoodsService;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.repository.UserRepository;
import com.example.peeppo.global.responseDto.ApiResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

import static com.example.peeppo.domain.goods.enums.Category.BOOK;

@DataJpaTest
@ActiveProfiles("security")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GoodsServiceTest {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void createGoodsTest(){
        GoodsRequestDto goodsRequestDto = new GoodsRequestDto("제목", "내용", BOOK, "서울특별시", "상", "직거래", true, 1000L);
        WantedRequestDto wantedRequestDto = new WantedRequestDto();
        List<MultipartFile> images = Collections.emptyList();

        User user = userRepository.findById(1L).orElseThrow(() -> new NullPointerException("존재하지 않는 유저입니다.")); // 적절한 사용자 정보를 생성해주세요


        ApiResponse<MsgResponseDto> response = goodsService.goodsCreate(goodsRequestDto, images, wantedRequestDto, user);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertNotNull(response.getInfo());
        Assertions.assertNull(response.getError());
    }

}