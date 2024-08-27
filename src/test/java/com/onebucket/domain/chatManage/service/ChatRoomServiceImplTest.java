package com.onebucket.domain.chatManage.service;

import com.onebucket.domain.chatManage.dao.ChatRoomRepository;
import com.onebucket.domain.chatManage.domain.ChatRoom;
import com.onebucket.domain.chatManage.dto.ChatMemberDto;
import com.onebucket.domain.chatManage.dto.CreateChatRoomDto;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.global.exceptionManage.customException.CommonException;
import com.onebucket.global.exceptionManage.customException.chatManageException.ChatManageException;
import com.onebucket.global.exceptionManage.customException.chatManageException.Exceptions.ChatRoomException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import com.onebucket.global.exceptionManage.errorCode.CommonErrorCode;
import com.onebucket.global.utils.ChatRoomValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * <br>package name   : com.onebucket.domain.chatManage.service
 * <br>file name      : ChatRoomServiceImplTest
 * <br>date           : 2024-08-04
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
 * 2024-08-04        SeungHoon              init create
 * </pre>
 */
@ExtendWith(MockitoExtension.class)
class ChatRoomServiceImplTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private Member mockMember;

    @Mock
    private ChatRoom mockChatRoom;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private ChatRoomValidator chatRoomValidator;

    @InjectMocks
    private ChatRoomServiceImpl chatRoomService;

    private ChatRoom chatRoom;
    private CreateChatRoomDto dto;
    @BeforeEach
    void setUp() {
        ChatMemberDto dto1 = ChatMemberDto.from("han");
        ChatMemberDto dto2 = ChatMemberDto.from("Lee");
        Set<ChatMemberDto> members = new HashSet<>();
        members.add(dto1);
        members.add(dto2);
        dto = CreateChatRoomDto.of(
                "room1", LocalDateTime.now(),"user1", members,10
        );
        chatRoom = ChatRoom.create(dto);
    }

    @Test
    @DisplayName("채팅방 만들기 성공")
    void createChatRoom_success() {
        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(mockChatRoom);
        when(mockChatRoom.getRoomId()).thenReturn("room1");
        chatRoomService.createChatRoom(dto);

        verify(chatRoomRepository, times(1)).save(any(ChatRoom.class));
    }

    @Test
    @DisplayName("채팅방 만들기 실패 - 유저 수가 너무 많음")
    void createChatRoom_fail() {
        CreateChatRoomDto dto = CreateChatRoomDto.of(
                "room1", LocalDateTime.now(),"user1", new HashSet<>(),-1
        );
        doThrow(new ChatRoomException(ChatErrorCode.MAX_MEMBERS_EXCEEDED, "채팅방 인원수가 너무 많습니다."))
                .when(chatRoomValidator).validateCreateChatRoom(any(CreateChatRoomDto.class));

        assertThrows(ChatRoomException.class, () -> chatRoomService.createChatRoom(dto));
    }

    @Test
    @DisplayName("채팅방 유저 추가 성공")
    void addChatMembers_success() {
        String roomId = "room1";
        String username = "user1";
        String nickname = "Jang";
        Member member = Member.builder().nickname(nickname).build();
        doReturn(Optional.of(member)).when(memberRepository).findByUsername(username);
        when(mongoTemplate.findAndModify(any(Query.class), any(Update.class),
                any(FindAndModifyOptions.class), eq(ChatRoom.class)))
                .thenReturn(chatRoom);

        chatRoomService.addChatMembers(roomId,username);

        verify(memberRepository, times(1)).findByUsername(username);
        verify(mongoTemplate,times(1)).findAndModify(any(Query.class), any(Update.class),
                any(FindAndModifyOptions.class), eq(ChatRoom.class));
    }

    @Test
    @DisplayName("채팅방 유저 추가 실패 - 존재하지 않는 유저")
    void addChatMembers_fail_UnknownUser() {
        String roomId = "room1";
        String username = "user1";
        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            chatRoomService.addChatMembers(roomId, username);
        });

        assertEquals(AuthenticationErrorCode.UNKNOWN_USER, exception.getErrorCode());
    }

    @Test
    @DisplayName("채팅방 유저 추가 실패 - 존재하지 않는 채팅방")
    void addChatMembers_fail_RoomNotFound() {
        String roomId = "room1";
        String username = "user1";
        doThrow(new ChatRoomException(ChatErrorCode.NOT_EXIST_ROOM, "존재하지 않는 채팅방입니다."))
                .when(chatRoomValidator).validateChatRoomExists(roomId);

        ChatRoomException exception = assertThrows(ChatRoomException.class, () -> {
            chatRoomService.addChatMembers(roomId, username);
        });

        assertEquals(ChatErrorCode.NOT_EXIST_ROOM, exception.getErrorCode());
    }

    @Test
    @DisplayName("채팅방에서 나가기 성공")
    void removeChatMembers_success() {
        String roomId = "room1";
        String username = "user1";
        String nickname = "han";
        Member member = Member.builder().nickname(nickname).build();
        doReturn(Optional.of(member)).when(memberRepository).findByUsername(username);
        doReturn(Optional.of(chatRoom)).when(chatRoomRepository).findByRoomId(roomId);
        when(mongoTemplate.findAndModify(any(Query.class), any(Update.class),
                any(FindAndModifyOptions.class), eq(ChatRoom.class)))
                .thenReturn(chatRoom);

        chatRoomService.removeChatMember(roomId,username);

        verify(memberRepository, times(1)).findByUsername(username);
        verify(mongoTemplate,times(1)).findAndModify(any(Query.class), any(Update.class),
                any(FindAndModifyOptions.class), eq(ChatRoom.class));
    }

    @Test
    @DisplayName("채팅방에서 나가기 실패 - 존재하지 않는 채팅방")
    void removeChatMembers_fail_ChatRoomNotFound() {
        String roomId = "room1";
        String username = "user1";
        doReturn(Optional.empty()).when(chatRoomRepository).findByRoomId(roomId);

        ChatRoomException exception = assertThrows(ChatRoomException.class, () -> {
            chatRoomService.removeChatMember(roomId, username);
        });

        assertEquals(ChatErrorCode.NOT_EXIST_ROOM, exception.getErrorCode());
    }

    @Test
    @DisplayName("채팅방에서 나가기 실패 - 존재하지 않는 유저")
    void removeChatMembers_fail_UnknownUser() {
        String roomId = "room1";
        String username = "user1";
        doReturn(Optional.of(chatRoom)).when(chatRoomRepository).findByRoomId(roomId);
        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            chatRoomService.removeChatMember(roomId, username);
        });

        assertEquals(AuthenticationErrorCode.UNKNOWN_USER, exception.getErrorCode());
    }

    @Test
    @DisplayName("채팅방에서 나가기 실패 - 채팅방에 속하지 않은 유저")
    void removeChatMembers_fail_UserNotInChatRoom() {
        String roomId = "room1";
        String username = "user1";
        String nickname = "notExistName";
        Member failedMember = Member.builder().nickname(nickname).build();
        // 유저와 채팅방이 모두 존재하지만, 유저가 채팅방에 속하지 않은 경우
        doReturn(Optional.of(chatRoom)).when(chatRoomRepository).findByRoomId(roomId);
        doReturn(Optional.of(failedMember)).when(memberRepository).findByUsername(username);
        doThrow(new ChatRoomException(ChatErrorCode.USER_NOT_IN_ROOM, "해당 유저는 이 채팅방의 멤버가 아닙니다."))
                .when(chatRoomValidator).validateMemberInChatRoom(chatRoom,failedMember);

        ChatRoomException exception = assertThrows(ChatRoomException.class, () -> {
            chatRoomService.removeChatMember(roomId, username);
        });

        assertEquals(ChatErrorCode.USER_NOT_IN_ROOM, exception.getErrorCode());
    }

    @Test
    @DisplayName("채팅방 목록 조회 성공")
    void getChatRooms_success() {
        doReturn(Arrays.asList(
                ChatRoom.builder(),
                ChatRoom.builder(),
                ChatRoom.builder()
        )).when(chatRoomRepository).findAll();
        final List<ChatRoom> chatRooms = chatRoomService.getChatRooms();
        assertThat(chatRooms).hasSize(3);
    }

    @Test
    @DisplayName("특정 채팅방 조회 성공")
    void getChatRoom_success() {
        String roomId = "room1";
        doReturn(Optional.of(chatRoom)).when(chatRoomRepository).findByRoomId(roomId);

        assertThat(chatRoomService.getChatRoom(roomId)).isEqualTo(chatRoom);
    }

    @Test
    @DisplayName("특정 채팅방 조회 실패 - 존재하지 않는 채팅방")
    void getChatRoom_fail() {
        String roomId = "room1";
        ChatRoomException result = assertThrows(ChatRoomException.class, () -> {
            chatRoomService.getChatRoom(roomId);
        });
        assertThat(result.getErrorCode()).isEqualTo(ChatErrorCode.NOT_EXIST_ROOM);
    }

    @Test
    @DisplayName("채팅방 삭제 성공")
    void deleteChatRoom_success() {
        String roomId = "room1";
        String username = "user1";
        doReturn(Optional.of(chatRoom)).when(chatRoomRepository).findByRoomId(roomId);
        doReturn(Optional.of(mockMember)).when(memberRepository).findByUsername(username);
        when(mongoTemplate.findAndModify(any(Query.class), any(Update.class),
                any(FindAndModifyOptions.class), eq(ChatRoom.class)))
                .thenReturn(chatRoom);

        chatRoomService.removeChatMember(roomId,username);
    }

    @Test
    @DisplayName("채팅방 삭제 실패 - 존재하지 않는 채팅방임")
    void removeChatMember_chatRoomDoesNotExist() {
        String roomId = "room1";
        String username = "user1";

        when(chatRoomRepository.findByRoomId(roomId)).thenReturn(Optional.empty());

        ChatRoomException result = assertThrows(ChatRoomException.class, () -> {
            chatRoomService.removeChatMember(roomId, username);
        });

        assertThat(result.getErrorCode()).isEqualTo(ChatErrorCode.NOT_EXIST_ROOM);
    }

    @Test
    @DisplayName("채팅방 삭제 실패 - 존재하지 않는 유저임")
    void removeChatMember_userDoesNotExist() {
        String roomId = "room1";
        String username = "user1";

        when(chatRoomRepository.findByRoomId(roomId)).thenReturn(Optional.of(new ChatRoom()));
        when(memberRepository.findByUsername(username)).thenReturn(Optional.empty());

        AuthenticationException result = assertThrows(AuthenticationException.class, () -> {
            chatRoomService.removeChatMember(roomId, username);
        });

        assertThat(result.getErrorCode()).isEqualTo(AuthenticationErrorCode.UNKNOWN_USER);
    }

    @Test
    @DisplayName("채팅방 삭제 실패 - 채팅방에 없는 유저임")
    void removeChatMember_userNotInChatRoom() {
        String roomId = "room1";
        String username = "user1";

        when(chatRoomRepository.findByRoomId(roomId)).thenReturn(Optional.of(chatRoom));
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));
        doThrow(new ChatManageException(ChatErrorCode.USER_NOT_IN_ROOM)).when(chatRoomValidator)
                .validateMemberInChatRoom(chatRoom, mockMember);

        ChatManageException result = assertThrows(ChatManageException.class, () -> {
            chatRoomService.removeChatMember(roomId, username);
        });

        assertThat(result.getErrorCode()).isEqualTo(ChatErrorCode.USER_NOT_IN_ROOM);
    }

    @Test
    @DisplayName("Remove Chat Member - Data Access Error")
    void removeChatMember_dataAccessError() {
        String roomId = "room1";
        String username = "user1";

        when(chatRoomRepository.findByRoomId(roomId)).thenReturn(Optional.of(chatRoom));
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));
        doNothing().when(chatRoomValidator).validateMemberInChatRoom(chatRoom, mockMember);

        when(mongoTemplate.findAndModify(any(Query.class), any(Update.class), any(FindAndModifyOptions.class), eq(ChatRoom.class)))
                .thenThrow(new CommonException(CommonErrorCode.DATA_ACCESS_ERROR));

        CommonException result = assertThrows(CommonException.class, () -> {
            chatRoomService.removeChatMember(roomId, username);
        });

        assertThat(result.getErrorCode()).isEqualTo(CommonErrorCode.DATA_ACCESS_ERROR);
    }
}