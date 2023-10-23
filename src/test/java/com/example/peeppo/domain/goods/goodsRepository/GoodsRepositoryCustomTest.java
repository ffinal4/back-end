//package com.example.peeppo.domain.goods.goodsRepository;
//
//
//import com.example.peeppo.domain.goods.entity.Goods;
//import com.example.peeppo.domain.goods.repository.goods.GoodsRepository;
//import com.example.peeppo.domain.user.entity.User;
//import com.example.peeppo.domain.user.repository.UserRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.Arrays;
//
//@DataJpaTest
//@ActiveProfiles("security")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class GoodsRepositoryCustomTest {
//
//    @Autowired
//    private GoodsRepository goodsRepository;
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    void findRandomGoods() {
//        int[] arr = new int[3];
//        arr[0] = 1;
//        arr[1] = 2;
//        arr[2] = 3;
//        Arrays.stream(arr).sorted();
//        User user = userRepository.findById(1L)
//                .orElseThrow(() -> new NullPointerException("존재하지 않는 유저"));
//
//        Goods goods = goodsRepository.findRandomGoods(user)
//                .orElseThrow(() -> new NullPointerException("존재하지 않는 상품"));
//        Assertions.assertNotNull(goods);
//    }
//}
