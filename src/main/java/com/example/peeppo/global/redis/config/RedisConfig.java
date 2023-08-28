package com.example.peeppo.global.redis.config;

import com.example.peeppo.domain.chat.entity.ChatRoom;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();

        redisConfiguration.setHostName(host);
        redisConfiguration.setPort(port);
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration);

        return lettuceConnectionFactory;
    }

    @Primary
    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class)); //Json포맷 형식으로 메시지를 교환하기 위해
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(String.class)); //Json포맷 형식으로 메시지를 교환하기 위해

        //* redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(ChatRoom.class)); //Json포맷 형식으로 메시지를 교환하기 위해
       // redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatRoom.class)); //Json포맷 형식으로 메시지를 교환하기 위해*//*


        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(ChatRoom.class));
        //redisTemplate.setHashKeySerializer(new Jackson2JsonRedisSerializer<>(ChatRoom.class));


      //  redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatRoomResponseDto.class));
       // redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(ChatRoomResponseDto.class));
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

//    @Primary
//    @Bean
//    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(connectionFactory);
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//
//        Jackson2JsonRedisSerializer<ChatRoom> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(ChatRoom.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule()); // Optional: If you're using Java 8 Date/Time API
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Ignore unknown properties
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY); // Access all fields
//        //jsonRedisSerializer.setObjectMapper(objectMapper);
//
//        redisTemplate.setValueSerializer(jsonRedisSerializer);
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(jsonRedisSerializer);
//
//        redisTemplate.afterPropertiesSet();
//
//        return redisTemplate;
//    }

    @Bean
    //redismessageListenercontainer 는 Redis channel(Topic)으로 부터 메시지를 받고,
    //주입된 리스너들에게 비동기적으로 dispatch하는 역할을 수행하는 컨테이너이다
    // 즉, redis에 발행된 (pub)된 메시지 처리를 위한 리스너 설정
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
                                                                       MessageListenerAdapter listenerAdapter,
                                                                       ChannelTopic channelTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, channelTopic);
        return container;
    }

    @Bean
    //위의 컨테이너로부터 메시지를 dispatch 받고 실제 메시지를 처리하는 로직인 subscriber bean을 추가해준다
    // 메시지를 구독자에게 보내는 역할
    public MessageListenerAdapter listenerAdapter(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "sendMessage");
    }

    //redis서버와 상호작용하기 위한 템플릿 관련 설정, redis 서버에는 bytes 코드만이 저장되므로
    //key와 value에 serializer을 설정해준다
//    @Bean
//    public RedisTemplate<String, Object> redis2Template
//    (RedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(connectionFactory);
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class)); //Json포맷 형식으로 메시지를 교환하기 위해
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(String.class)); //Json포맷 형식으로 메시지를 교환하기 위해
//
//       //* redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(ChatRoom.class)); //Json포맷 형식으로 메시지를 교환하기 위해
//        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatRoom.class)); //Json포맷 형식으로 메시지를 교환하기 위해*//*
//
//        return redisTemplate;
//    }

    @Bean
    public RedisTemplate<String, Object> chatRoomRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatRoom.class));
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(ChatRoom.class));
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    //Topic 공유를 위해 Channel Topic을 빈으로 등록해 단일화 시켜준다.
    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic("chatroom");
    }
}

