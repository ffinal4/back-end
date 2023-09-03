package com.example.peeppo.domain.chat.handler;

import com.example.peeppo.domain.chat.entity.ChatMessage;
import com.example.peeppo.domain.chat.entity.ChatRoom;
import com.example.peeppo.domain.chat.service.ChatService;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.repository.UserRepository;
import com.example.peeppo.domain.user.service.UserService;
import com.example.peeppo.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

// session의 connect / disconnect 시점을 알 수 있다.
@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
    //websocket을 통해서 들어온 요청이 처리 되기 전 실행된다
    private final ChatService chatService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        System.out.println("웹소켓에 신호 들어옴");
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message); // 각종 웹소켓 정보 가져올 수 있다



        if (StompCommand.CONNECT == accessor.getCommand()) { // websocket 연결요청
            System.out.println("웹소켓 연결 요청");
            String jwtToken = accessor.getFirstNativeHeader("AccessToken");
            System.out.println("토큰 확인 토큰 값:"+ jwtToken);

        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) { // 채팅룸 구독요청
            // header정보에서 구독 destination정보를 얻고, roomId를 추출한다.
            // roomId를 URL로 전송해주고 있어 추출 필요
            System.out.println("구독 요청");
            String roomId = chatService.getRoomId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));

            // 채팅방에 들어온 클라이언트 sessionId를 roomId와 맵핑해 놓는다.(나중에 특정 세션이 어떤 채팅방에 들어가 있는지 알기 위함)
            System.out.println("해당 룸 ID:" +roomId);
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            System.out.println("요청한 session ID :"+ sessionId);

            System.out.println("입장 요청, 유저정보 셋팅 요청");
            chatService.setUserEnterInfo(sessionId, roomId);


            System.out.println("입장 메세지 발송 요청 진입");
           // String token = Optional.ofNullable(accessor.getFirstNativeHeader("AccessToken")).orElse("UnknownUser");
            String authorizationHeader = accessor.getFirstNativeHeader("AccessToken");
            System.out.println(authorizationHeader);
            if(authorizationHeader == null || authorizationHeader.equals("null")){
                throw new MessageDeliveryException("메세지 예외");
            }
            String token = authorizationHeader.substring(BEARER_PREFIX.length());
            String email = jwtUtil.getUserMail(token);
            User user = userRepository.findByEmail(email).orElse(null);
            ChatRoom chatRoom = chatService.findRoomById(roomId);
            chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.ENTER).chatRoom(chatRoom).userId(user.getUserId()).build());

            System.out.println("발송 요청");
            log.info("SUBSCRIBED {}, {}", user.getNickname(), roomId);

        } else if (StompCommand.DISCONNECT == accessor.getCommand()) { // Websocket 연결 종료

            System.out.println("연결 종료 단계");
            // 연결이 종료된 클라이언트 sesssionId로 채팅방 id를 얻는다.
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            System.out.println("연결이 종료된 세션 : "+sessionId);

            String roomId = chatService.getUserEnterRoomId(sessionId);
            System.out.println("룸 아이디 확인 : "+ roomId);
            // 클라이언트 퇴장 메시지를 채팅방에 발송한다.(redis publish)
        //    String name = Optional.ofNullable((Principal) message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");

            String token = Optional.ofNullable(accessor.getFirstNativeHeader("AccessToken")).orElse("UnknownUser");
            String email = jwtUtil.getUserMail(token);
            User user = userRepository.findByEmail(email).orElse(null);
            ChatRoom chatRoom = chatService.findRoomById(roomId);
            chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.LEAVE).chatRoom(chatRoom).userId(user.getUserId()).build());

            // 퇴장한 클라이언트의 roomId 맵핑 정보를 삭제한다.
            chatService.removeUserEnterInfo(sessionId);
            System.out.println("맵핑 정보 삭제");
            log.info("DISCONNECTED {}, {}", sessionId, roomId);
        }
        return message;
    }


}
