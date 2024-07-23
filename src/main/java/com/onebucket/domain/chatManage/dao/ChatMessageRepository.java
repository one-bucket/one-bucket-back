//package com.onebucket.domain.chatManage.dao;
//
//import com.onebucket.domain.chatManage.domain.ChatMessage;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.ListOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Repository;
//
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//
//import static com.onebucket.domain.chatManage.Const.CHAT_MESSAGE_ORIGIN;
//
///**
// * <br>package name   : com.onebucket.domain.chatManage.dao
// * <br>file name      : ChatMessageRepository
// * <br>date           : 2024-07-09
// * <pre>
// * <span style="color: white;">[description]</span>
// *
// * </pre>
// * <pre>
// * <span style="color: white;">usage:</span>
// * {@code
// *
// * } </pre>
// * <pre>
// * modified log :
// * =======================================================
// * DATE           AUTHOR               NOTE
// * -------------------------------------------------------
// * 2024-07-09        SeungHoon              init create
// * </pre>
// */
//@Repository
//@RequiredArgsConstructor
//public class ChatMessageRepository {
//    private final RedisTemplate<String, Object> redisTemplate;
//    private ListOperations<String, Object> listOperations;
//
//    @PostConstruct
//    public void init() {
//        listOperations = redisTemplate.opsForList();
//    }
//    /**
//     * 채팅 로그를 redis에 3일 동안 저장한다. 기간은 수정 간으하다.
//     * @param chatMessage
//     */
//    public void saveChatMessage(ChatMessage chatMessage) {
//        String key = CHAT_MESSAGE_ORIGIN + chatMessage.getRoomId();
//        listOperations.rightPush(key, chatMessage);
//        redisTemplate.expire(key,3, TimeUnit.DAYS);
//    }
//
//    public List<ChatMessage> getChatMessagesFromChatRoom(String roomId) {
//        // roomId를 기반으로 해당 채팅방의 모든 메시지를 조회
//        String key = CHAT_MESSAGE_ORIGIN + roomId;
//        List<Object> messages = listOperations.range(key, 0, -1);
//        if(messages == null || messages.isEmpty()) {
//            return new ArrayList<>();
//        }
//        // Object 리스트를 ChatMessage 리스트로 변환.
//        List<ChatMessage> chatMessages = new ArrayList<>(messages.stream()
//                .map(message -> (ChatMessage) message)
//                .toList());
//        // timestamp 순으로 정렬
//        chatMessages.sort(Comparator.comparing(ChatMessage::getCreateAt));
//        return chatMessages;
//    }
//}
