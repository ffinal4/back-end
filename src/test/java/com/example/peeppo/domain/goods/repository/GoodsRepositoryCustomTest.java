package com.example.peeppo.domain.goods.repository;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/*
@DataJpaTest
@ActiveProfiles("security")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GoodsRepositoryCustomTest {

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void findRandomGoods() {
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new NullPointerException("tq"));

        Goods goods = goodsRepository.findRandomGoods(user);
        Assertions.assertNotNull(goods);
    }
}*/
