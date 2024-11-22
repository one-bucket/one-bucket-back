package com.onebucket.domain.chatManager.service;

import com.onebucket.domain.chatManager.dto.ChatRoomDto;

/**
 * <br>package name   : com.onebucket.domain.chatManager.service
 * <br>file name      : MappingMemberAndChatroomService
 * <br>date           : 2024-11-20
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
public interface MappingMemberAndChatroomService {

    String getExistChatroom(ChatRoomDto.SearchMapper dto);

    void saveChatRoomMapping(ChatRoomDto.SaveMapper dto);
}

