package com.example.peeppo.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket //WebSocket 활성화, 웹소켓에 대해 대부분 자동설정을 한다.
public class WebSocketConfig implements WebSocketConfigurer { // 추가적인 설정을 한다
    //클라이언트가 보내는 정보를 받아서 처리할 Handler를 만들어주고 이를 연결할 websocket 주소를 설정해주어야한다.
    private final WebSocketHandler webSocketHandler;

    // WebSocketHandler를 추가한다.
    // 1. 클라이언트가 접속을 했을 때 특정 메소드가 호출
    // 2. 클라이언트가 접속을 close했을 때 특정 메소드가 호출
    // 3. 클라이언트가 메시지를 보냈을 때 특정 메소드 호출


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry){
        webSocketHandlerRegistry.addHandler(webSocketHandler, "ws/chat").setAllowedOrigins("*");
    } //ws는 웹소켓 엔드포인트 url  (웹소켓은 브라우저가 http로 요청을 보낸후, 서버가 웹소켓을 지원해주면 웹 소켓연결로 바뀐다.)


}
