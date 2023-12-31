package com.example.peeppo.domain.chat.service;

import com.example.peeppo.domain.chat.dto.*;
import com.example.peeppo.domain.chat.entity.ChatMessage;
import com.example.peeppo.domain.chat.entity.ChatRoom;
import com.example.peeppo.domain.chat.entity.UserChatRoomRelation;
import com.example.peeppo.domain.chat.repository.ChatMessageRepository;
import com.example.peeppo.domain.chat.repository.ChatRoomRepository;
import com.example.peeppo.domain.chat.repository.UserChatRoomRelationRepository;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.repository.goods.GoodsRepository;
import com.example.peeppo.domain.goods.repository.request.RequestRepository;
import com.example.peeppo.domain.image.entity.UserImage;
import com.example.peeppo.domain.image.repository.UserImageRepository;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.repository.UserRepository;
import com.example.peeppo.global.security.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.peeppo.global.security.jwt.JwtUtil.BEARER_PREFIX;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final ChannelTopic channelTopic;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final GoodsRepository goodsRepository;

    private final UserImageRepository userImageRepository;
    private final UserChatRoomRelationRepository userChatRoomRelationRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private Map<String, ChatRoom> chatRooms;
    private final JwtUtil jwtUtil;

    // Redis CacheKeys
    private static final String CHAT_ROOMS = "CHAT_ROOM"; // 채팅룸 저장
    public static final String USER_COUNT = "USER_COUNT"; // 채팅룸에 입장한 클라이언트수 저장
    public static final String ENTER_INFO = "ENTER_INFO"; // 채팅룸에 입장한 클라이언트의 sessionId와 채팅룸 id를 맵핑한 정보 저장

    @Resource(name = "chatRoomRedisTemplate")
    private HashOperations<String, String, ChatRoomResponseDto> hashOpsChatRoom;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsEnterInfo;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;


    @PostConstruct // 의존성 주입이 이루어진 후 초기화 작업이 필요한 에 사용
    private void init(){
        chatRooms = new LinkedHashMap<>();
    } //순서대로 저장메서드


// 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다. -> 이것으로 채팅방은 지워지지 않음
    // 물건Id랑 user로 저장한다
    @Transactional
    public ChatRoom createRoom (Long goodsId , ChatRoomRequestDto chatRoomRequestDto, User user){
        String randomId = UUID.randomUUID().toString();
        Goods goods = goodsRepository.findById(goodsId).orElseThrow(() ->
                new NullPointerException("해당 게시글은 존재하지 않습니다."));
        User enterUser = userRepository.findById(user.getUserId()).orElseThrow(()->new IllegalArgumentException("해당하는 사용자는 없습니다"));
        User buyerUser = userRepository.findById(chatRoomRequestDto.getBuyerId()).orElseThrow(()->new IllegalArgumentException("해당하는 사용자는 없습니다"));
        //seller의 goods로 채팅방 만들어
        ChatRoom chatRoom = new ChatRoom(goods, randomId);
        chatRoomRepository.save(chatRoom);
        hashOpsChatRoom.put(CHAT_ROOMS, randomId, new ChatRoomResponseDto(chatRoom));
        UserChatRoomRelation userChatRoomRelation = new UserChatRoomRelation(enterUser, chatRoom, buyerUser);
        userChatRoomRelationRepository.save(userChatRoomRelation);
       ChatMessage chatMessage = new ChatMessage(ChatMessage.MessageType.ENTER, chatRoom,user,"물물교환 신청이 수락되었습니다", String.valueOf(chatRoom.getCreatedAt()));
       chatMessageRepository.save(chatMessage);
       System.out.println(hashOpsChatRoom.get(CHAT_ROOMS, randomId));
        return chatRoom;
    }
    /**
     * destination정보에서 roomId 추출
     */
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return destination.substring(lastIndex + 1);
        else
            return "";
    }

    // 유저가 입장한 채팅방ID와 유저 세션ID 맵핑 정보 저장
    public void setUserEnterInfo(String sessionId, String roomId) {
        hashOpsEnterInfo.put(ENTER_INFO, sessionId, roomId);
    }

    // 유저 세션으로 입장해 있는 채팅방 ID 조회
    public String getUserEnterRoomId(String sessionId) {
        return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
    }


    // 유저 세션정보와 맵핑된 채팅방ID 삭제
    public void removeUserEnterInfo(String sessionId) {
        hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
    }

    // 채팅방 유저수 조회
    public long getUserCount(String roomId) {
        return Long.valueOf(Optional.ofNullable(valueOps.get(USER_COUNT + "_" + roomId)).orElse("0"));
    }

    // 채팅방에 입장한 유저수 +1
    public long plusUserCount(String roomId) {
        return Optional.ofNullable(valueOps.increment(USER_COUNT + "_" + roomId)).orElse(0L);
    }

    // 채팅방에 입장한 유저수 -1
    public long minusUserCount(String roomId) {
        return Optional.ofNullable(valueOps.decrement(USER_COUNT + "_" + roomId)).filter(count -> count > 0).orElse(0L);
    }

    //전체 채팅방 조회 => 사용자 마다 !
    public ResponseEntity<List<ChatRoomFindAllRoomDto>> findAllRoom(User user){
        List<UserChatRoomRelation> userChatRoomRelation = userChatRoomRelationRepository.findAllBySellerUserIdOrBuyerUserId(user.getUserId(), user.getUserId());
        List<ChatRoomFindAllRoomDto> chatRoomFindAllRoomDtos = new ArrayList<>();
        List<ChatRoomResponseDto> chatRoomResponseDto = new ArrayList<>();
        UserImage myImage = userImageRepository.finduserImage(user.getUserId());
        for(UserChatRoomRelation userChatRoom : userChatRoomRelation){
            UserImage otherImage = null;
            String nickname = null;
            if(user.getUserId() == userChatRoom.getBuyer().getUserId()){
                otherImage = userImageRepository.finduserImage(userChatRoom.getSeller().getUserId());
                nickname = userChatRoom.getSeller().getNickname();
            }
            if(user.getUserId() == userChatRoom.getSeller().getUserId()) {
                otherImage = userImageRepository.finduserImage(userChatRoom.getBuyer().getUserId());
                nickname = userChatRoom.getBuyer().getNickname();
            }
            ChatMessage chatMessage = chatMessageRepository.findChatRoomId(userChatRoom.getChatRoom().getId());
            if(otherImage != null){
                chatRoomResponseDto.add(new ChatRoomResponseDto(userChatRoom, chatMessage, otherImage, nickname));
            }else{
                chatRoomResponseDto.add(new ChatRoomResponseDto(userChatRoom, chatMessage, nickname));
            }
        }
        if(myImage == null){
            chatRoomFindAllRoomDtos.add(new ChatRoomFindAllRoomDto(chatRoomResponseDto));
        }
        else {
            chatRoomFindAllRoomDtos.add(new ChatRoomFindAllRoomDto(myImage.getImageUrl(), chatRoomResponseDto));
        }
            return ResponseEntity.status(HttpStatus.OK.value()).body(chatRoomFindAllRoomDtos);
    }



    //            boolean checkSellerUser = false;
//            if(Objects.equals(user.getUserId(), userChatRoom.getSeller().getUserId())){
//                checkSellerUser = true;
//            }
//            UserImage sellerImage = userImageRepository.finduserImage(userChatRoom.getSeller().getUserId());
//            UserImage buyerImage = userImageRepository.finduserImage(userChatRoom.getBuyer().getUserId());
//            if(sellerImage == null){
//
//            }

    //roomId 기준으로 채팅방 찾기
    public ChatRoom findRoomById(String roomId) {
        return chatRoomRepository.findByRoomId(roomId);
    }

    //roomId 기준으로 채팅방 메시지 내용 찾기
    @Transactional(readOnly = true)
    public List<ChatMessageResponseDto> findMessageById(String roomId, User user) {
        ChatRoom chatRoom = findRoomById(roomId);
        List<ChatMessage> chatMessageList = chatMessageRepository.findAllChatRoomId(chatRoom.getId());
        List<ChatMessageResponseDto> chatMessageResponseDtos = new ArrayList<>();
        for(ChatMessage chatMessage : chatMessageList){
            boolean checkUser = false;
            User messageUser = userRepository.findById(chatMessage.getSenderId()).orElse(null);
            if(chatMessage.getSenderId() == user.getUserId()){
                checkUser = true;
            }
            ChatMessageResponseDto chatMessageResponseDto = new ChatMessageResponseDto(chatMessage, messageUser, checkUser);
            chatMessageResponseDtos.add(chatMessageResponseDto);
        }
        return chatMessageResponseDtos;
    }

//    @Transactional(readOnly = true)
//    public Slice<ChatMessageResponseDto> findMessageById(String roomId, User user, Pageable page) {
//        ChatRoom chatRoom = findRoomById(roomId);
//        Slice<ChatMessage> chatMessageList = chatMessageRepository.findChatMessagesByChatRoomIdOrderByTimeDesc(chatRoom.getId(), page);
//        return chatMessageList.map(chatMessage -> {
//            User messageUser = userRepository.findById(chatMessage.getSenderId()).orElseThrow(()->
//                    new NullPointerException("유저는 없어요"));
//            boolean checkUser = (Objects.equals(chatMessage.getSenderId(), user.getUserId()));
//            return new ChatMessageResponseDto(chatMessage, messageUser, checkUser);
//        });
//    }

    @Transactional
    public void saveMessage(String roomId ,ChatMessageRequestDto chatMessageRequestDto, String token){
        System.out.println("메세지 발송 단계 진입");
        long systemTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String dTime = formatter.format(systemTime);

        ChatRoom chatRoom = findRoomById(roomId);

        String authToken = token.substring(BEARER_PREFIX.length());
        String email = jwtUtil.getUserMail(authToken);
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("일치하는 사용자가 없습니다"));
        String username = user.getNickname();

        ChatMessage chatMessage = new ChatMessage(chatMessageRequestDto, chatRoom, dTime, user);

        chatMessageRepository.save(chatMessage);
        System.out.println("전송 요청");
        //template.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
        System.out.println("전송 완료");
    }

    public void deleteChatRoom(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId);
        chatRoomRepository.delete(chatRoom);
    }

    public void sendChatMessage(ChatMessage chatMessage, User user) {
        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            chatMessage.sendMessage(user.getNickname() + "님이 입장했습니다");
            System.out.println(chatMessage);
        } else if (ChatMessage.MessageType.LEAVE.equals(chatMessage.getType())) {
            chatMessage.sendMessage(user.getNickname() + "님이 퇴장했습니다");
            System.out.println(chatMessage);
        }
        chatMessageRepository.save(chatMessage);
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }

    public ChatRoom addUserToChatRoom(String roomId, User user) {
        User enterUser = userRepository.findById(user.getUserId()).orElseThrow(()->new IllegalArgumentException("해당하는 사용자는 없습니다"));
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId);
        UserChatRoomRelation userChatRoomRelation = new UserChatRoomRelation(enterUser, chatRoom);
        userChatRoomRelationRepository.save(userChatRoomRelation);
        return chatRoom;
    }
}


