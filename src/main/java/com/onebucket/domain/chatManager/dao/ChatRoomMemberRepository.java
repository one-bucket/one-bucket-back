package com.onebucket.domain.chatManager.dao;

import com.onebucket.domain.chatManager.entity.ChatRoomMember;
import com.onebucket.domain.chatManager.entity.ChatRoomMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.chatManager.dao
 * <br>file name      : ChatRoomMemberRepository
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
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, ChatRoomMemberId> {

    @Query("SELECT c.chatRoom.id FROM ChatRoomMember c WHERE c.member.id = :memberId")
    List<String> findChatRoomIdByMemberId(Long memberId);
}
