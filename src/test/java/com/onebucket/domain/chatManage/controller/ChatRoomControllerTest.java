package com.onebucket.domain.chatManage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.onebucket.domain.chatManage.domain.ChatRoom;
import com.onebucket.domain.chatManage.dto.chatmessage.ChatMessageDto;
import com.onebucket.domain.chatManage.dto.chatroom.ChatMemberDto;
import com.onebucket.domain.chatManage.dto.chatroom.CreateChatRoomDto;
import com.onebucket.domain.chatManage.dto.chatroom.RequestCreateChatRoomDto;
import com.onebucket.domain.chatManage.dto.chatroom.ResponseChatRoomListDto;
import com.onebucket.domain.chatManage.service.ChatRoomService;
import com.onebucket.global.exceptionManage.customException.chatManageException.ChatManageException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import com.onebucket.global.exceptionManage.exceptionHandler.BaseExceptionHandler;
import com.onebucket.global.exceptionManage.exceptionHandler.ChatExceptionHandler;
import com.onebucket.global.exceptionManage.exceptionHandler.DataExceptionHandler;
import com.onebucket.global.utils.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static com.onebucket.testComponent.testUtils.JsonFieldResultMatcher.hasKey;
import static com.onebucket.testComponent.testUtils.JsonFieldResultMatcher.hasStatus;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <br>package name   : com.onebucket.domain.chatManage.controller
 * <br>file name      : ChatRoomControllerTest
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
class ChatRoomControllerTest {
    @Mock
    private ChatRoomService chatRoomService;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private ChatRoomController chatRoomController;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(chatRoomController)
                .setControllerAdvice(new BaseExceptionHandler(), new DataExceptionHandler(),new ChatExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("채팅방 목록 조회 성공")
    void getRooms_success() throws Exception {
        final String url = "/chat/rooms";
        doReturn(
                Collections.emptyList()
        ).when(chatRoomService).getChatRooms();
        final ResultActions resultActions = mockMvc.perform(
                get(url)
        );
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("채팅방 생성 성공")
    void getRoom_success() throws Exception {
        final String url = "/chat/room";
        Set<ChatMemberDto> members = new HashSet<>();
        members.add(ChatMemberDto.from("user1"));
        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(objectMapper.writeValueAsString(new RequestCreateChatRoomDto("room1","user1",members,10)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        resultActions.andExpect(status().isCreated());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidChatRoomDtos")
    @DisplayName("채팅방 생성 실패 - 입력 값에 null이 있음")
    void createRoom_fail(RequestCreateChatRoomDto requestCreateChatRoomDto) throws Exception {
        final String url = "/chat/room";

        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(objectMapper.writeValueAsString(requestCreateChatRoomDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("채팅방 삭제 성공")
    void deleteRoom_success() throws Exception {
        String roomId = "room1";
        String username = "user1";
        doReturn(username).when(securityUtils).getCurrentUsername();

        final String url = "/chat/room/" + roomId;
        final ResultActions resultActions = mockMvc.perform(
                delete(url)
        );

        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("채팅방 삭제 실패 - not exist authentication in token while getCurrentUsername")
    void deleteRoom_fail() throws Exception {
        String internalMessage = "Not exist authentication in ContextHolder";
        AuthenticationErrorCode code = AuthenticationErrorCode.NON_EXIST_AUTHENTICATION;
        AuthenticationException exception = new AuthenticationException(code, internalMessage);
        when(securityUtils.getCurrentUsername()).thenThrow(exception);
        String roomId = "room1";
        final String url = "/chat/room/" + roomId;

        final ResultActions resultActions = mockMvc.perform(
                delete(url)
        );

        resultActions.andExpect(hasStatus(code))
                .andExpect(hasKey(code, internalMessage));
    }

    @Test
    @DisplayName("채팅방 메세지 불러오기 성공")
    void getChatMessage_success() throws Exception {
        String roomId = "room1";
        doReturn(new ArrayList<ChatMessageDto>()).when(chatRoomService).getChatMessages(roomId);
        String url = "/chat/messages/" + roomId;
        final ResultActions resultActions = mockMvc.perform(
                get(url)
        );
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("채팅방 메세지 불러오기 실패 - 서비스 예외 발생")
    void getMessages_fail_serviceException() throws Exception {
        final String roomId = "roomId";
        doThrow(new ChatManageException(ChatErrorCode.INTERNAL_ERROR)).when(chatRoomService).getChatMessages(roomId);

        final String url = "/chat/messages/" + roomId;

        final ResultActions resultActions = mockMvc.perform(
                get(url)
        );

        resultActions.andExpect(hasStatus(ChatErrorCode.INTERNAL_ERROR));
    }

    static Stream<RequestCreateChatRoomDto> provideInvalidChatRoomDtos() {
        LocalDateTime time = LocalDateTime.of(2024, 1, 1, 12, 0);
        return Stream.of(
                    new RequestCreateChatRoomDto(null,"user1",new HashSet<>(),10),  // name is null
                    new RequestCreateChatRoomDto("room1",null,new HashSet<>(),10),  // createdBy is null
                    new RequestCreateChatRoomDto("room1","user1",null,10), // members is null
                    new RequestCreateChatRoomDto("room1","user1",new HashSet<>(),null) // maxMembers Set is null
        );
    }
}