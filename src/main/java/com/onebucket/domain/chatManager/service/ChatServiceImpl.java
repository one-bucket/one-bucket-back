package com.onebucket.domain.chatManager.service;

import com.onebucket.domain.chatManager.dto.ChatDto;
import com.onebucket.domain.chatManager.mongo.ChatMessageRepository;
import com.onebucket.domain.chatManager.mongo.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.chatManager.service
 * <br>file name      : ChatServiceImpl
 * <br>date           : 2024-10-17
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 */
@Service
@RequiredArgsConstructor
public class ChatServiceImpl {
    private final ChatMessageRepository chatMessageRepository;


    public void saveMessage(ChatDto chatDto) {
        // 새로운 ChatMessage 도큐먼트 생성
        ChatMessage newMessage = ChatMessage.builder()
                .roomId(chatDto.getRoomId())  // roomId 설정
                .message(chatDto.getMessage())
                .sender(chatDto.getSender())
                .timestamp(chatDto.getTime())
                .build();

        chatMessageRepository.save(newMessage);
    }

    public List<ChatMessage> getMessageAfterTimestamp(String roomId, Date timestamp) {
        // 동기적으로 메시지 조회
        return chatMessageRepository.findMessagesAfterTimestamp(roomId, timestamp);
    }


}
