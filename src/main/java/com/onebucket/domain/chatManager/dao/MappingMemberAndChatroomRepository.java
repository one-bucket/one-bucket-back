package com.onebucket.domain.chatManager.dao;

import com.onebucket.domain.chatManager.entity.MappingMemberAndChatroom;
import com.onebucket.domain.chatManager.entity.MappingMemberAndChatroomId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <br>package name   : com.onebucket.domain.chatManager.dao
 * <br>file name      : MappingMemberAndChatroomRepository
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

@Repository
public interface MappingMemberAndChatroomRepository extends JpaRepository<MappingMemberAndChatroom, MappingMemberAndChatroomId> {
}
