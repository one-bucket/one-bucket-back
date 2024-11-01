package com.onebucket.domain.chatManager.service;

import com.onebucket.domain.chatManager.dto.ChatDto;
import com.onebucket.domain.chatManager.dto.ChatRoomDto;


/**
 * <br>package name   : com.onebucket.domain.chatManager.service
 * <br>file name      : ChatService
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
public interface ChatService {

    void saveMessage(ChatDto chatDto);
    String saveImage(String base64Image, ChatRoomDto.SaveImage dto);

}
