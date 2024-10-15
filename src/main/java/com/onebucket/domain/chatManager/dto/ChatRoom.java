package com.onebucket.domain.chatManager.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <br>package name   : com.onebucket.domain.chatManager.dto
 * <br>file name      : ChatRoom
 * <br>date           : 2024-09-16
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

@Getter
@Setter
public class ChatRoom {
    private String roomId;
    private String roomName;
    private Long userCount;
    private ConcurrentHashMap<String, String> userList = new ConcurrentHashMap<String, String>();
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    public ChatRoom create(String roomName) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.userCount = 1L;
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.roomName = roomName;

        return chatRoom;
    }
}
