package com.onebucket.domain.chatManage.service;

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
    ChatRoom createChatRoom(CreateChatRoomDto createChatRoomDto);
    void enterChatRoom(String roomId);
    List<ChatRoom> getChatRooms();
    ChatRoom getChatRoom(String roomId);
    ChannelTopic getTopic(String roomId);
    void addChatRoomMember(String roomId, String username);
}
