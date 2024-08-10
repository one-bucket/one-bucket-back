package com.onebucket.domain.chatManage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.domain.chatManage.service.ChatMessageService;
import com.onebucket.global.exceptionManage.customException.chatManageException.ChatManageException;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import com.onebucket.global.exceptionManage.exceptionHandler.BaseExceptionHandler;
import com.onebucket.global.exceptionManage.exceptionHandler.ChatExceptionHandler;
import com.onebucket.global.exceptionManage.exceptionHandler.DataExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static com.onebucket.testComponent.testUtils.JsonFieldResultMatcher.hasStatus;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <br>package name   : com.onebucket.domain.chatManage.controller
 * <br>file name      : ChatMessageControllerTest
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
class ChatMessageControllerTest {

    @Mock
    private ChatMessageService chatMessageService;

    @InjectMocks
    private ChatMessageController chatMessageController;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(chatMessageController)
                .setControllerAdvice(new ChatExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("채팅방 메세지 불러오기 성공")
    void getChatMessage_success() throws Exception {
        String roomId = "room1";
        doReturn(new ArrayList<ChatMessage>()).when(chatMessageService).getChatMessages(roomId);
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
        doThrow(new ChatManageException(ChatErrorCode.INTERNAL_ERROR)).when(chatMessageService).getChatMessages(roomId);

        final String url = "/chat/messages/" + roomId;

        final ResultActions resultActions = mockMvc.perform(
                get(url)
        );

        resultActions.andExpect(hasStatus(ChatErrorCode.INTERNAL_ERROR));
    }
}