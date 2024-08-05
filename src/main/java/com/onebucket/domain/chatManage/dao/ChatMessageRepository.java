package com.onebucket.domain.chatManage.dao;

import com.onebucket.domain.chatManage.domain.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;


/**
 * <br>package name   : com.onebucket.domain.chatManage.dao
 * <br>file name      : ChatMessageRepository
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
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByRoomIdOrderByCreatedAtDesc(String roomId);
}