package com.example.peeppo.global.config;

import com.example.peeppo.domain.chat.handler.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class SpringChatConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        // stomp 접속 주소 url => /ws-stomp
        registry.addEndpoint("/stomp/chat") //연결될 엔드포인트 (엔드 포인트 : 통신의 도착지점 , 즉 이 엔드포인트에 특정한 통신이 도착하면 어떤 행위를 하게 만든다 )
                .setAllowedOriginPatterns("*");
               // .withSockJS(); //socketJs를 연결한다는 설정 => 폴백을 통해 애플리케이션은 WebSocket API를 사용할 수 있지만 런타임에 필요할 때 WebSocket이 아닌 대안으로 적절하게 저하된다

    }

    @Override //상위 클래스 메서드 재정의
    public void configureMessageBroker(MessageBrokerRegistry registry){
        //메시지를 구독하는 요청 url => 즉 메시지 받을때 , 해당 경로로 SimpleBroker를 등록. SimpleBroker는 해당하는 경로를 SUBSCRIBE하는 Client에게 메세지를 전달하는 간단한 작업을 수행
        registry.enableSimpleBroker("/sub");
        //메시지를 발행하는 요청 url => 즉 메시지 보낼때
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler); // StompHandler가 Websocket 앞단에서 token을 체크할 수 있도록 인터셉터로 설정
    }


}
