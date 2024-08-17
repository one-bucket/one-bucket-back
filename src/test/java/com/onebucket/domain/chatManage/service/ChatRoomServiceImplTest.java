package com.onebucket.domain.chatManage.service;

import com.onebucket.domain.chatManage.dao.ChatRoomRepository;
import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.domain.chatManage.domain.ChatRoom;
import com.onebucket.domain.chatManage.dto.ChatMemberDto;
import com.onebucket.domain.chatManage.dto.CreateChatRoomDto;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.global.exceptionManage.customException.CommonException;
import com.onebucket.global.exceptionManage.customException.chatManageException.Exceptions.ChatRoomException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import com.onebucket.global.exceptionManage.errorCode.CommonErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
    private ChatMessage mockChatMessage;

    @Mock
    private ChatRoom mockChatRoom;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private ChatRoomServiceImpl chatRoomService;

    @Test
    @DisplayName("채팅방 만들기 성공")
    void createChatRoom_success() {
        CreateChatRoomDto dto = CreateChatRoomDto.of(
                "room1", LocalDateTime.now(),"user1", new HashSet<>(),10
        );
        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(mockChatRoom);

        chatRoomService.createChatRoom(dto);

        verify(chatRoomRepository, times(1)).save(any(ChatRoom.class));
    }

    @Test
    @DisplayName("채팅방 만들기 실패 - 유저 수가 너무 많음")
    void createChatRoom_fail() {
        CreateChatRoomDto dto = CreateChatRoomDto.of(
                "room1", LocalDateTime.now(),"user1", new HashSet<>(),-1
        );
        assertThrows(ChatRoomException.class, () -> chatRoomService.createChatRoom(dto));
    }

    @Test
    @DisplayName("채팅방 유저 추가 성공")
    void addChatMembers_success() {
        String roomId = "room1";
        String username = "user1";
        Member mockMember = mock(Member.class);
        doReturn(Optional.of(mockMember)).when(memberRepository).findByUsername(username);
        doReturn(Optional.of(mockChatRoom)).when(chatRoomRepository).findByRoomId(roomId);
        when(mongoTemplate.findAndModify(any(Query.class), any(Update.class),
                any(FindAndModifyOptions.class), eq(ChatRoom.class)))
                .thenReturn(mockChatRoom);

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
        doReturn(Optional.of(mockChatRoom)).when(chatRoomRepository).findByRoomId(roomId);
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
        doReturn(Optional.empty()).when(chatRoomRepository).findByRoomId(roomId);

        ChatRoomException exception = assertThrows(ChatRoomException.class, () -> {
            chatRoomService.addChatMembers(roomId, username);
        });

        assertEquals(ChatErrorCode.NOT_EXIST_ROOM, exception.getErrorCode());
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
        doReturn(Optional.of(mockChatRoom)).when(chatRoomRepository).findByRoomId(roomId);

        assertThat(chatRoomService.getChatRoom(roomId)).isEqualTo(mockChatRoom);
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
}