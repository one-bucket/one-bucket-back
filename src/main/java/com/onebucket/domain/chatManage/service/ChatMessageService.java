package com.onebucket.domain.chatManage.service;

import com.onebucket.domain.chatManage.domain.ChatMessage;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.chatManage.service
 * <br>file name      : ChatMessageService
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
public interface ChatMessageService {
    void saveChatMessage(ChatMessage chatMessage);
    List<ChatMessage> getChatMessages(String roomId);
}
