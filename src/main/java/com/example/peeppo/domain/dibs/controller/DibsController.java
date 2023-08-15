//package com.example.peeppo.domain.dibs.controller;
//
//import com.example.peeppo.domain.dibs.dto.DibsRequestDto;
//import com.example.peeppo.domain.dibs.service.DibsService;
//import com.example.peeppo.domain.user.dto.CheckResponseDto;
//import com.example.peeppo.global.security.UserDetailsImpl;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api")
//@RequiredArgsConstructor
//public class DibsController {
//
//    private final DibsService dibsService;
//
//    @PostMapping("/scrap")
//    public ResponseEntity<CheckResponseDto> dibsGoods(@AuthenticationPrincipal UserDetailsImpl userDetails,
//                                                      @Valid @RequestBody DibsRequestDto dibsRequestDto) {
//        return dibsService.dibsGoods(userDetails.getUser(), dibsRequestDto);
//    }
//}
