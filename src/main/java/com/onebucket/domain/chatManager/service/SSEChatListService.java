package com.onebucket.domain.chatManager.service;

import com.onebucket.domain.chatManager.dto.ChatDto;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * <br>package name   : com.onebucket.domain.chatManager.service
 * <br>file name      : SSEChatListService
 * <br>date           : 2024-10-28
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
public interface SSEChatListService {
    SseEmitter subscribe(Long userId);
    void notifyRoomUpdate(ChatDto chat);
}
