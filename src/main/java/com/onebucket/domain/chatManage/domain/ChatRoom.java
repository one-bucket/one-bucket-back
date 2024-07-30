package com.onebucket.domain.chatManage.domain;

import com.onebucket.domain.memberManage.dto.ChatMemberDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * <br>package name   : com.onebucket.domain.chatManage.domain
 * <br>file name      : ChatRoom
 * <br>date           : 2024-07-08
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
 * 2024-07-08        SeungHoon              init create
 * </pre>
 */
@Getter
@Builder
@Document(collection = "chatroom")
public class ChatRoom  {

    private String roomId;
    private String name;

    private Set<ChatMemberDto> members;

    public void addMember(ChatMemberDto member) {
        members.add(member);
    }
}
