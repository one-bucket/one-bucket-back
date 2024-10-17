package com.onebucket.domain.chatManager.service;

import com.onebucket.domain.chatManager.dto.ChatRoomDto;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.chatManager.service
 * <br>file name      : ChatRoomService
 * <br>date           : 10/16/24
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
public interface ChatRoomService {

    List<ChatRoomDto.MemberInfo> getMemberList(String roomId);

    String createRoom(ChatRoomDto.CreateRoom dto);

    ChatRoomDto.GetTradeInfo getTradeInfo(String roomId);

    void changeRoomName(String roomName);

    void deleteRoom(String roomId);

}
