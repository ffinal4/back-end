/*
package com.example.peeppo.domain.home.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


// 필요한 Bean 만 호출해서 사용
//@WebMvcTest(HomeController.class)
@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    private static final String BASE_URL = "/api/home";

    @Test
    @DisplayName("Home 컨트롤러 테스트")
    void getHomeTest() throws Exception {
        // Mock 객체가 특정 상황에서 해야하는 행위를 정의하는 메소드
       // given(homeService.peeppoHome(null)).willReturn(
       //         new HomeResponseDto(new GoodsListResponseDto(new Goods(), "sdfdf", false, true), new RatingUserResponseDto(), new AuctionResponseDto());
        mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL))
                .andExpect(content().string("로그인 성공"));
    }


}
*/
