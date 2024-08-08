package com.onebucket.domain.chatManage.dao;

import com.onebucket.domain.chatManage.domain.ChatRoom;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
/**
 * <br>package name   : com.onebucket.domain.chatManage.dao
 * <br>file name      : ChatRoomRepository
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
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    @NotNull
    Optional<ChatRoom> findByRoomId(@NotNull String id);

    @Query("{ 'members.nickname': ?0 }")
    List<ChatRoom> findByMembersNickname(String nickname);
}
