package com.onebucket.domain.chatManage.dto.chatroom;

import com.onebucket.domain.chatManage.domain.ChatRoom;
import com.onebucket.domain.chatManage.dto.chatmessage.ChatMessageDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * <br>package name   : com.onebucket.domain.chatManage.dto.chatroom
 * <br>file name      : ResponseChatRoomDto
 * <br>date           : 2024-09-11
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 * <pre>
 * modified log :
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-09-11        SeungHoon              init create
 * </pre>
 */
public record ResponseChatRoomListDto (
        String roomName,
        String roomCreatedBy,
        LocalDateTime roomCreatedAt,
        int maxMembers,
        Set<ChatMemberDto> memberDtos,
        String lastContent,
        String lastContentSender,
        LocalDateTime ContentCreatedAt
) {
    public static ResponseChatRoomListDto from(ChatRoom chatRoom) {
        List<ChatMessageDto> messages = chatRoom.getMessages();
        ChatMessageDto lastMessage = null;
        if (messages != null && !messages.isEmpty()) {
            lastMessage = messages.get(messages.size() - 1); // 마지막 메시지 가져오기
        }
        return new ResponseChatRoomListDto(
                chatRoom.getName(),
                chatRoom.getCreatedBy(),
                LocalDateTime.now(),
                chatRoom.getMaxMembers(),
                chatRoom.getMembers(),
                lastMessage != null ? lastMessage.message() : null,
                lastMessage != null ? lastMessage.sender() : null,
                lastMessage != null ? lastMessage.createdAt() : null
        );
    }
}
