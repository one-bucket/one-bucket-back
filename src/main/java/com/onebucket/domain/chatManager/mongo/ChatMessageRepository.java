package com.onebucket.domain.chatManager.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.chatManager.mongo
 * <br>file name      : ChatLogsRepository
 * <br>date           : 2024-10-17
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
@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    @Query("{ 'roomId': ?0, 'timestamp': { $gt: ?1 } }")
    List<ChatMessage> findMessagesAfterTimestamp(String roomId, Date timestamp);

}
