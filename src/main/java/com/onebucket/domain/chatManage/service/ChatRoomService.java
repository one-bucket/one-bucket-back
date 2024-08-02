package com.onebucket.domain.chatManage.service;

import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.domain.chatManage.domain.ChatRoom;
import com.onebucket.domain.chatManage.dto.CreateChatRoomDto;
import org.springframework.data.redis.listener.ChannelTopic;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.chatManage.service
 * <br>file name      : ChatRoomService
 * <br>date           : 2024-07-09
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
 * 2024-07-09        SeungHoon              init create
 * </pre>
 */
public interface ChatRoomService {
    void createChatRoom(CreateChatRoomDto createChatRoomDto);
    void enterChatRoom(String roomId,String username);
    List<ChatRoom> getChatRooms();
    ChatRoom getChatRoom(String roomId);
    ChannelTopic getTopic(String roomId);
    void addChatMessage(ChatMessage chatMessage);
    void addChatRoomMember(String roomId, String username);
}
