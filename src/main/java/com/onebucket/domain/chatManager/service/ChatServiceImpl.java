package com.onebucket.domain.chatManager.service;

import com.onebucket.domain.chatManager.dto.ChatDto;
import com.onebucket.domain.chatManager.mongo.ChatMessageRepository;
import com.onebucket.domain.chatManager.mongo.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


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
public class ChatServiceImpl implements ChatService {
    private final ChatMessageRepository chatMessageRepository;


    @Override
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



}
