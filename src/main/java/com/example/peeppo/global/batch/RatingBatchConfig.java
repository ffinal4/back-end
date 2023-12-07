package com.example.peeppo.global.batch;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.repository.goods.GoodsRepository;
import com.example.peeppo.domain.rating.helper.RatingHelper;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;


import static com.example.peeppo.domain.goods.enums.GoodsStatus.ONSALE;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RatingBatchConfig {

    private final JobLauncher jobLauncher;
//    private final JobBuilder jobBuilder;
//    private final StepBuilder stepBuilder;
    private final EntityManagerFactory entityManagerFactory;
    private final GoodsRepository goodsRepository;
    private final RatingHelper ratingHelper;

    private final static int TRANSACTION_CHUNK_SIZE = 50;

    @Bean
    public ItemReader<Goods> itemReader() {
        RepositoryItemReader<Goods> reader = new RepositoryItemReader<>();
        reader.setRepository(goodsRepository);
        reader.setMethodName("findAllByCursor");
//        reader.setArguments(new Object[]{0L, TRANSACTION_CHUNK_SIZE});
        reader.setPageSize(TRANSACTION_CHUNK_SIZE);
//        reader.setEntityManager(entityManagerFactory.createEntityManager());
//        reader.setSorts(Map.of("goodsId", Sort.Direction.ASC));
        return reader;
    }

    @Bean
    public ItemProcessor<Goods, Goods> itemProcessor() {
        return goods -> {
            if (ONSALE.equals(goods.getGoodsStatus())) {
                ratingHelper.resetGoodsAvgPrice(goods);
            }
            return goods;
        };
    }

    @Bean
    public ItemWriter<Goods> itemWriter() {
        return goodsList -> {
            for (Goods goods : goodsList) {
            }
        };
    }

//    @Bean
//    public Step resetPricesStep(ItemReader<Goods> itemReader,
//                                ItemProcessor<Goods, Goods> itemProcessor,
//                                ItemWriter<Goods> itemWriter) {
//        return stepBuilder.get("resetPricesStep")
//                .<Goods, Goods>chunk(TRANSACTION_CHUNK_SIZE)
//                .reader(itemReader)
//                .processor(itemProcessor)
//                .writer(itemWriter)
//                .build();
//    }

//    @Bean
//    public Job resetPricesJob(Step resetPricesStep) {
//        return jobBuilder.get("resetPricesJob")
//                .flow(resetPricesStep)
//                .end()
//                .build();
//    }

}