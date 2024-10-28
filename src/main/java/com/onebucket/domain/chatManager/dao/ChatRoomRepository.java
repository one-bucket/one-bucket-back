package com.onebucket.domain.chatManager.dao;

import com.onebucket.domain.chatManager.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <br>package name   : com.onebucket.domain.chatManager.dao
 * <br>file name      : ChatRoomRepository
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
@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {


}
