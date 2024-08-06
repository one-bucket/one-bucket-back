package com.onebucket.domain.chatManage.service;

import com.onebucket.domain.chatManage.dao.ChatRoomRepository;
import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.domain.chatManage.domain.ChatRoom;
import com.onebucket.domain.chatManage.dto.CreateChatRoomDto;
import com.onebucket.domain.chatManage.pubsub.RedisSubscriber;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.memberManage.dto.CreateMemberRequestDto;
import com.onebucket.global.exceptionManage.customException.CommonException;
import com.onebucket.global.exceptionManage.customException.chatManageException.Exceptions.RoomNotFoundException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import com.onebucket.global.exceptionManage.errorCode.CommonErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    private RedisMessageListenerContainer redisMessageListener;

    @Mock
    private ChatRoom mockChatRoom;

    @InjectMocks
    private ChatRoomServiceImpl chatRoomService;

    public CreateChatRoomDto getRoomDto() {
        return CreateChatRoomDto.of(
                "room1", LocalDateTime.now(),"user1"
        );
    }

    public CreateMemberRequestDto getMemberDto() {
        return CreateMemberRequestDto.builder()
                .nickname("han")
                .build();
    }

    @BeforeEach
    void init() {
        Map<String, ChannelTopic> topics = new ConcurrentHashMap<>();
        ReflectionTestUtils.setField(chatRoomService, "topics", topics);
        ReflectionTestUtils.setField(chatRoomService, "redisMessageListener", redisMessageListener);
    }

    @Test
    @DisplayName("채팅방 만들기 성공")
    void createChatRoom_success() {
        CreateChatRoomDto dto = getRoomDto();
        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(mockChatRoom);

        chatRoomService.createChatRoom(dto);

        verify(chatRoomRepository, times(1)).save(any(ChatRoom.class));
    }

    @Test
    @DisplayName("채팅방 입장 성공")
    void enterChatRoom_success() {
        String roomId = "room1";
        String username = "user1";
        Member mockMember = mock(Member.class);
        ChatRoom mockChatRoom = mock(ChatRoom.class);
        doReturn(Optional.of(mockMember)).when(memberRepository).findByUsername(username);
        doReturn(Optional.of(mockChatRoom)).when(chatRoomRepository).findByRoomId(roomId);

        chatRoomService.enterChatRoom(roomId,username);

        // verify를 사용하여 메서드 호출 횟수 검증
        verify(chatRoomRepository, times(1)).findByRoomId(roomId);
        verify(memberRepository, times(1)).findByUsername(username);
        verify(chatRoomRepository,times(1)).save(any(ChatRoom.class));
    }


    @Test
    @DisplayName("채팅방 입장 실패 - 존재하지 않는 유저")
    void testEnterChatRoom_UnknownUser() {
        String roomId = "room1";
        String username = "user1";

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            chatRoomService.enterChatRoom(roomId, username);
        });

        assertEquals(AuthenticationErrorCode.UNKNOWN_USER, exception.getErrorCode());
    }

    @Test
    @DisplayName("채팅방 입장 실패 - 존재하지 않는 채팅방")
    void testEnterChatRoom_RoomNotFound() {
        String roomId = "room1";
        String username = "user1";
        Member mockMember = mock(Member.class);
        doReturn(Optional.of(mockMember)).when(memberRepository).findByUsername(username);

        RoomNotFoundException exception = assertThrows(RoomNotFoundException.class, () -> {
            chatRoomService.enterChatRoom(roomId, username);
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
        ChatRoom mockChatRoom = mock(ChatRoom.class);
        doReturn(Optional.of(mockChatRoom)).when(chatRoomRepository).findByRoomId(roomId);

        assertThat(chatRoomService.getChatRoom(roomId)).isEqualTo(mockChatRoom);
    }

    @Test
    @DisplayName("특정 채팅방 조회 실패 - 존재하지 않는 채팅방")
    void getChatRoom_fail() {
        String roomId = "room1";
        RoomNotFoundException result = assertThrows(RoomNotFoundException.class, () -> {
            chatRoomService.getChatRoom(roomId);
        });
        assertThat(result.getErrorCode()).isEqualTo(ChatErrorCode.NOT_EXIST_ROOM);
    }

    @Test
    @DisplayName("채팅 추가 성공")
    void addChatMessage_success() {
        String roomId = "room1";
        ChatMessage mockChatMessage = mock(ChatMessage.class);
        // mock 객체가 아니라서 doReturn 사용하지 않는다.
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(roomId)
                .messages(new ArrayList<>())
                .build();
        when(mockChatMessage.getRoomId()).thenReturn(roomId);
        doReturn(Optional.of(chatRoom)).when(chatRoomRepository).findByRoomId(roomId);

        chatRoomService.addChatMessage(mockChatMessage);

        verify(chatRoomRepository, times(1)).findByRoomId(roomId);
        verify(chatRoomRepository, times(1)).save(chatRoom);
        assertTrue(chatRoom.getMessages().contains(mockChatMessage));
    }

    @Test
    @DisplayName("채팅 추가 실패 - 채팅이 null 값임")
    void addChatMessage_fail() {
        assertThrows(NullPointerException.class, () -> chatRoomService.addChatMessage(null));
    }

    @Test
    @DisplayName("채팅 추가 실패 - 메세지에 roomId가 없음")
    void addChatMessage_fail_nullRoomId() {
        ChatMessage mockChatMessage = mock(ChatMessage.class);
        when(mockChatMessage.getRoomId()).thenReturn(null);

        assertThrows(RoomNotFoundException.class, () -> chatRoomService.addChatMessage(mockChatMessage));
    }

    @Test
    @DisplayName("채팅 추가 실패 - 데이터베이스 저장 문제 발생")
    void addChatMessage_fail_databaseSaveFailure() {
        String roomId = "room1";
        ChatMessage mockChatMessage = mock(ChatMessage.class);
        when(mockChatMessage.getRoomId()).thenReturn(roomId);

        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(roomId)
                .messages(new ArrayList<>())
                .build();

        doReturn(Optional.of(chatRoom)).when(chatRoomRepository).findByRoomId(roomId);
        doThrow(new CommonException(CommonErrorCode.DATA_ACCESS_ERROR)).when(chatRoomRepository).save(chatRoom);
        assertThrows(CommonException.class, () -> chatRoomService.addChatMessage(mockChatMessage));
    }
}