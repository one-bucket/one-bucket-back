package com.onebucket.global.utils;

import com.onebucket.domain.chatManage.dao.ChatRoomRepository;
import com.onebucket.domain.chatManage.domain.ChatRoom;
import com.onebucket.domain.chatManage.dto.CreateChatRoomDto;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.global.exceptionManage.customException.chatManageException.Exceptions.ChatRoomException;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
/**
 * <br>package name   : com.onebucket.global.utils
 * <br>file name      : ChatRoomValidator
 * <br>date           : 2024-08-19
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
 * 2024-08-19        SeungHoon              init create
 * </pre>
 */

@Component
@RequiredArgsConstructor
public class ChatRoomValidator {

    private final ChatRoomRepository chatRoomRepository;

    public void validateChatRoomExists(String roomId) {
        if (chatRoomRepository.findByRoomId(roomId).isEmpty()) {
            throw new ChatRoomException(ChatErrorCode.NOT_EXIST_ROOM, "존재하지 않는 채팅방입니다.");
        }
    }

    public void validateChatRoomCreator(String username, String createdBy) {
        if (!username.equals(createdBy)) {
            throw new ChatRoomException(ChatErrorCode.USER_NOT_CREATOR);
        }
    }

    public void validateCreateChatRoom(CreateChatRoomDto dto) {
        if(dto.members().size() > dto.maxMembers()) {
            throw new ChatRoomException(ChatErrorCode.MAX_MEMBERS_EXCEEDED,"채팅방 인원수가 너무 많습니다.");
        }
        // 추가적인 유효성 검사 로직 필요시 여기에 추가
    }

    public void validateMemberInChatRoom(ChatRoom chatRoom, Member member) {
        boolean isMember = chatRoom.getMembers().stream()
                .anyMatch(memberDto -> memberDto.nickname().equals(member.getNickname()));

        if (!isMember) {
            throw new ChatRoomException(ChatErrorCode.USER_NOT_IN_ROOM, "해당 유저는 이 채팅방의 멤버가 아닙니다.");
        }
    }
}
