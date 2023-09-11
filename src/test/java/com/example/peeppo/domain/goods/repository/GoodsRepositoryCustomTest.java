package com.example.peeppo.domain.goods.repository;

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
