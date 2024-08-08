package com.onebucket.domain.chatManage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.onebucket.domain.chatManage.domain.ChatRoom;
import com.onebucket.domain.chatManage.dto.ChatMemberDto;
import com.onebucket.domain.chatManage.dto.CreateChatRoomDto;
import com.onebucket.domain.chatManage.service.ChatRoomService;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.exceptionHandler.BaseExceptionHandler;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static com.onebucket.testComponent.testUtils.JsonFieldResultMatcher.hasKey;
import static com.onebucket.testComponent.testUtils.JsonFieldResultMatcher.hasStatus;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
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
                .setControllerAdvice(new BaseExceptionHandler(), new DataExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("채팅방 목록 조회 성공")
    void getRooms_success() throws Exception {
        final String url = "/chat/rooms";
        doReturn(
                Arrays.asList(
                        ChatRoom.builder().build(),
                        ChatRoom.builder().build(),
                        ChatRoom.builder().build()
                )
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
                        .content(objectMapper.writeValueAsString(CreateChatRoomDto.of(
                                "room1",
                                LocalDateTime.of(2024, 1, 1, 12, 0),
                                "user1",
                                members
                                )
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        resultActions.andExpect(status().isCreated());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidChatRoomDtos")
    @DisplayName("채팅방 생성 실패 - 입력 값에 null이 있음")
    void createRoom_fail(CreateChatRoomDto createChatRoomDto) throws Exception {
        final String url = "/chat/room";

        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(objectMapper.writeValueAsString(createChatRoomDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("채팅방 입장 성공")
    void enterRoom_success() throws Exception {
        String username = "user1";
        String roomId = "room1";
        final String url = "/chat/room/" + roomId;
        doReturn(username).when(securityUtils).getCurrentUsername();
        doReturn(ChatRoom.builder().build()).when(chatRoomService).getChatRoom(roomId);

        final ResultActions resultActions = mockMvc.perform(
                get(url)
        );

        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("채팅방 입장 실패 - not exist authentication in token while getCurrentUsername")
    void enterRoom_fail() throws Exception {
        String internalMessage = "Not exist authentication in ContextHolder";
        AuthenticationErrorCode code = AuthenticationErrorCode.NON_EXIST_AUTHENTICATION;
        AuthenticationException exception = new AuthenticationException(code, internalMessage);
        when(securityUtils.getCurrentUsername()).thenThrow(exception);

        String roomId = "room1";
        final String url = "/chat/room/" + roomId;

        final ResultActions resultActions = mockMvc.perform(
                get(url)
        );

        resultActions.andExpect(hasStatus(code))
                .andExpect(hasKey(code, internalMessage));
    }


    static Stream<CreateChatRoomDto> provideInvalidChatRoomDtos() {
        LocalDateTime time = LocalDateTime.of(2024, 1, 1, 12, 0);
        return Stream.of(
                CreateChatRoomDto.of(null, time, "user1",new HashSet<>()),  // name is null
                CreateChatRoomDto.of("room1", null, "user1",new HashSet<>()),  // createdAt is null
                CreateChatRoomDto.of("room1",  time, null,new HashSet<>()), // createdBy is null
                CreateChatRoomDto.of("room1",time,"user1",null)
        );
    }









}