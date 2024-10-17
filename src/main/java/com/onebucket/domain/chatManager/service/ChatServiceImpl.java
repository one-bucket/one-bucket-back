package com.onebucket.domain.chatManager.service;

import com.onebucket.domain.chatManager.dao.ChatRepository;
import com.onebucket.domain.chatManager.dao.ChatRoomRepository;
import com.onebucket.domain.chatManager.dto.ChatDto;
import com.onebucket.domain.chatManager.mongo.ChatLog;
import com.onebucket.domain.chatManager.mongo.ChatLogsRepository;
import com.onebucket.domain.chatManager.mongo.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    private final ChatLogsRepository chatLogsRepository;


    public void saveMessage(ChatDto chatDto) {
        chatLogsRepository.findById(chatDto.getRoomId())
                .switchIfEmpty(Mono.just(new ChatLog(chatDto.getRoomId())))
                .flatMap(chatLog -> {
                    ChatMessage newMessage = ChatMessage.builder()
                            .message(chatDto.getMessage())
                            .sender(chatDto.getSender())
                            .timestamp(chatDto.getTime())
                            .build();
                    chatLog.addMessage(newMessage);
                    return chatLogsRepository.save(chatLog);

                })
                .subscribe();
    }

    public Flux<ChatMessage> getMessageAfterTimestamp(String roomId, String timestamp) {
        return chatLogsRepository.findMessagesAfterTimestamp(roomId, timestamp);
    }


}
