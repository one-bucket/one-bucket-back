package com.onebucket.domain.chatManage.service;

import com.onebucket.domain.chatManage.domain.ChatRoom;
import org.springframework.data.redis.listener.ChannelTopic;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    ChatRoom createChatRoom(String name);
    void enterChatRoom(String roomId);
    List<ChatRoom> getChatRooms();
    ChatRoom getChatRoom(String roomId);
    ChannelTopic getTopic(String roomId);
    void addMember(String roomId,String username);
}
