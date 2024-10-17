package com.onebucket.domain.chatManager.mongo;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

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
public interface ChatLogsRepository extends ReactiveMongoRepository<ChatLog, String> {
    @Query("{ 'roomId': ?0, 'messages.timestamp': { $gt: ?1 } }")
    Flux<ChatMessage> findMessagesAfterTimestamp(String roomId, String timestamp);

}
