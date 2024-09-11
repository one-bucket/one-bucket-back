package com.onebucket.domain.chatManage.service;

import com.onebucket.domain.chatManage.domain.ChatRoom;
import com.onebucket.domain.chatManage.dto.chatmessage.ChatMessageDto;
import com.onebucket.domain.chatManage.dto.chatroom.CreateChatRoomDto;
import com.onebucket.domain.chatManage.dto.chatroom.ResponseChatRoomListDto;
import org.springframework.web.multipart.MultipartFile;

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
    // 채팅방 로직
    String createChatRoom(CreateChatRoomDto createChatRoomDto);
    ChatRoom addChatMembers(String roomId, String nickname);
    ChatRoom removeChatMember(String roomId, String nickname);
    List<ResponseChatRoomListDto> getChatRooms();
    void addChatMessages(ChatMessageDto chatMessage);
    List<ResponseChatRoomListDto> findByMembersNickname(String nickname);
    void deleteChatRoom(String roomId,String username);

    // 채팅 메세지 로직
    List<ChatMessageDto> getChatMessages(String roomId);
    String uploadChatImage(MultipartFile file, String username);
}
