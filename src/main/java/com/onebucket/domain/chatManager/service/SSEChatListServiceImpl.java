package com.onebucket.domain.chatManager.service;

import com.onebucket.domain.chatManager.dto.ChatDto;
import com.onebucket.domain.chatManager.dto.ChatRoomDto;
import com.onebucket.domain.chatManager.dto.RoomUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <br>package name   : com.onebucket.domain.chatManager.service
 * <br>file name      : SSEChatListServiceImpl
 * <br>date           : 2024-10-27
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
public class SSEChatListServiceImpl {

    private final ChatRoomService chatRoomService;

    private Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        sseEmitters.put(userId, emitter);
        emitter.onCompletion(() -> sseEmitters.remove(userId));
        emitter.onTimeout(() -> sseEmitters.remove(userId));
        return emitter;
    }

    public void notifyRoomUpdate(ChatDto chat) {
        String roomId = chat.getRoomId();
        String latestMessage = chat.getMessage();

        List<Long> userIds = chatRoomService.getMemberList(roomId)
                .stream().map(ChatRoomDto.MemberInfo::getId).toList();

        for(Long userId : userIds) {
            SseEmitter emitter = sseEmitters.get(userId);
            if(emitter != null ) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("room-update")
                            .data(RoomUpdateDto.builder()
                                    .roomId(roomId)
                                    .latestMessage(latestMessage)
                                    .build()));
                } catch(IOException e) {
                    emitter.complete();
                    sseEmitters.remove(userId);
                }

            }
        }
    }
}
