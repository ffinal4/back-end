package com.example.peeppo.domain.chat.service;

import com.example.peeppo.domain.chat.dto.ChatMessageResponseDto;
import com.example.peeppo.domain.chat.dto.ChatRoomResponseDto;
import com.example.peeppo.domain.chat.entity.ChatMessage;
import com.example.peeppo.domain.chat.entity.ChatRoom;
import com.example.peeppo.domain.chat.repository.ChatMessageRepository;
import com.example.peeppo.domain.chat.repository.ChatRoomRepository;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.service.GoodsService;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.repository.UserRepository;
import com.example.peeppo.global.security.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final ChannelTopic channelTopic;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final GoodsService goodsService;
    private final UserRepository userRepository;
    private Map<String, ChatRoom> chatRooms;

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


//     채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다. -> 이것으로 채팅방은 지워지지 않음
    @Transactional
    public ChatRoomResponseDto createRoom(Long id, User user){
        String randomId = UUID.randomUUID().toString();
        Goods goods = goodsService.findGoods(id);
        User enterUser = userRepository.findById(user.getUserId()).orElseThrow(()->new IllegalArgumentException("해당하는 사용자는 없습니다"));
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .goods(goods)
                .user(enterUser)
                .build();
        hashOpsChatRoom.put(CHAT_ROOMS, randomId, new ChatRoomResponseDto(chatRoom));
        System.out.println(hashOpsChatRoom.get(CHAT_ROOMS, randomId));
        chatRoomRepository.save(chatRoom);
        return new ChatRoomResponseDto(chatRoom);
    }
    //채팅방 아이디는 랜덤 !

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
    public List<ChatRoom> findAllRoom(User user){
        // 채팅방 생성 순서를 최근순으로 반환
        //List chatRoomList = new ArrayList<>(chatRooms.values());
       // Collections.reverse(chatRoomList);
        chatRoomRepository.findByUserUserId(user.getUserId());
        return chatRoomRepository.findAll();
    }

    //roomId 기준으로 채팅방 찾기
    public ChatRoom findRoomById(String roomId) {
        return chatRoomRepository.findByRoomId(roomId);
    }

    //roomId 기준으로 채팅방 메시지 내용 찾기
    public List<ChatMessageResponseDto> findMessageById(String roomId, UserDetailsImpl userDetails) {
        List<ChatMessage> chatMessageList = chatMessageRepository.findAllByRoomIdAndUserUserId(roomId, userDetails.getUser().getUserId());
        List<ChatMessageResponseDto> chatMessageResponseDtos = new ArrayList<>();
        for(ChatMessage chatMessage : chatMessageList){
            ChatMessageResponseDto chatMessageResponseDto = new ChatMessageResponseDto(chatMessage);
            chatMessageResponseDtos.add(chatMessageResponseDto);
        }
        return chatMessageResponseDtos;
    }

    //채팅방 유저 리스트에 유저 추가
/*
    public String addUser(String roomId, String user){
        ChatRoom chatRoom = chatRooms.get(roomId);
        chatRoom.addSellerId(user);
        return user;
    }
*/

    // 채팅방 유저 리스트 삭제
    public void delUser(String roomId, String user){
        ChatRoom room = chatRooms.get(roomId);
        room.remove(user);
    }
    public String getUserName(String roomId){
        ChatRoom room = chatRooms.get(roomId);
        return room.getUser().getNickname();
    }

    public void saveMessage(ChatMessage chatMessage, User user){
        System.out.println("메세지 발송 단계 진입");
        long systemTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String dTime = formatter.format(systemTime);
        chatMessage.setTime(dTime);
        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            System.out.println(chatMessage);

            chatMessage.sendPerson(user.getNickname());
            chatMessage.sendMessage(chatMessage.getSender() + "님이 방에 입장했습니다.");

            System.out.println(chatMessage);

        } else if (ChatMessage.MessageType.LEAVE.equals(chatMessage.getType())) {
            System.out.println(chatMessage);

            chatMessage.sendMessage(chatMessage.getSender() + "님이 방에서 나갔습니다.");

            System.out.println(chatMessage);
        }

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
}
