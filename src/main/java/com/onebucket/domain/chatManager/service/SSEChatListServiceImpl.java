package com.onebucket.domain.chatManager.service;

import com.onebucket.domain.chatManager.dto.ChatDto;
import com.onebucket.domain.chatManager.dto.RoomUpdateDto;
import com.onebucket.global.exceptionManage.customException.chatManageException.Exceptions.ChatRoomException;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
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
public class SSEChatListServiceImpl implements SSEChatListService {

    private final ChatRoomService chatRoomService;
    private final Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        sseEmitters.put(userId, emitter);

        // Emitter 완료 시 제거
        emitter.onCompletion(() -> sseEmitters.remove(userId));
        emitter.onTimeout(() -> sseEmitters.remove(userId));
        emitter.onError(e -> sseEmitters.remove(userId)); // 추가: 에러 발생 시 제거

        return emitter;
    }

    public void notifyRoomUpdate(ChatDto chat) {
        String roomId = chat.getRoomId();
        String recentMessage = chat.getMessage();
        Date recentMessageTime = chat.getTime();

        // 현재 SecurityContext를 가져옴
        SecurityContext context = SecurityContextHolder.getContext();

        // 방 멤버 가져오기
        List<Long> userIds = chatRoomService.getMemberIds(roomId);

        for (Long userId : userIds) {
            SseEmitter emitter = sseEmitters.get(userId);

            if (emitter != null) {
                // 새로운 스레드에서 SecurityContext 설정
                CompletableFuture.runAsync(() -> {
                    SecurityContextHolder.setContext(context); // SecurityContext 전파

                    try {
                        // Emitter에 이벤트 전송
                        emitter.send(SseEmitter.event()
                                .name("room-update")
                                .data(RoomUpdateDto.builder()
                                        .roomId(roomId)
                                        .recentMessage(recentMessage)
                                        .recentMessageTime(recentMessageTime)
                                        .build()));
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                        throw new ChatRoomException(ChatErrorCode.INTERNAL_ERROR);
                    } finally {
                        // 스레드 종료 후 SecurityContext 정리
                        SecurityContextHolder.clearContext();
                    }
                });
            }
        }
    }
}
