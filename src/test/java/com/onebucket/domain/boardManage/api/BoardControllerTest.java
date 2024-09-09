package com.onebucket.domain.boardManage.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.onebucket.domain.boardManage.dto.internal.board.CreateBoardDto;
import com.onebucket.domain.boardManage.dto.internal.board.CreateBoardsDto;
import com.onebucket.domain.boardManage.dto.request.RequestCreateBoardDto;
import com.onebucket.domain.boardManage.service.BoardService;
import com.onebucket.global.exceptionManage.customException.boardManageException.AdminManageBoardException;
import com.onebucket.global.exceptionManage.errorCode.BoardErrorCode;
import com.onebucket.global.exceptionManage.errorCode.ValidateErrorCode;
import com.onebucket.global.exceptionManage.exceptionHandler.BaseExceptionHandler;
import com.onebucket.global.exceptionManage.exceptionHandler.DataExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.onebucket.testComponent.testUtils.JsonFieldResultMatcher.hasKey;
import static com.onebucket.testComponent.testUtils.JsonFieldResultMatcher.hasStatus;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * packageName : <span style="color: orange;">com.onebucket.domain.boardManage.api</span> <br>
 * name : <span style="color: orange;">BoardControllerTest</span> <br>
 * <p>
 * <span style="color: white;">[description]</span>
 * </p>
 * see Also: <br>
 *
 * <pre>
 * code usage:
 * {@code
 *
 * }
 * modified log:
 * ==========================================================
 * DATE          Author           Note
 * ----------------------------------------------------------
 * 9/2/24        isanghyeog         first create
 *
 * </pre>
 */
@ExtendWith(MockitoExtension.class)
class BoardControllerTest {

    @Mock
    private BoardService boardService;

    private MockMvc mockMvc;

    @InjectMocks
    private BoardController boardController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        RequestBuilder defaultBuilder = MockMvcRequestBuilders
                .get("/")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc = MockMvcBuilders.standaloneSetup(boardController)
                .setControllerAdvice(new BaseExceptionHandler(), new DataExceptionHandler())
                .defaultRequest(defaultBuilder)
                .build();
    }

    @Test
    @DisplayName("createBoard - success")
    void testCreateBoard_success() throws Exception {
        when(boardService.createBoard(any(CreateBoardDto.class))).thenReturn(1L);
        RequestCreateBoardDto dto = RequestCreateBoardDto.builder()
                .boardType("board type")
                .university("university")
                .name("name")
                .description("description")
                .build();
        mockMvc.perform(post("/board/create")
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(hasKey("message", "success create board"))
                .andExpect(hasKey("id", "1"));
    }

    @Test
    @DisplayName("createBoard - fail / invalid data")
    void testCreateBoard_fail_invalidData() throws Exception {

        RequestCreateBoardDto dto = RequestCreateBoardDto.builder()
                .boardType("board type")
                .university("university")
                .name("")
                .description("description")
                .build();

        ValidateErrorCode code = ValidateErrorCode.INVALID_DATA;
        mockMvc.perform(post("/board/create")
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code))
                .andExpect(content().string(containsString(
                        "name: must not be blank"
                )));
    }

    @Test
    @DisplayName("createBoard - fail / integration error")
    void testCreateBoard_fail_sqlException() throws Exception {
        RequestCreateBoardDto dto = RequestCreateBoardDto.builder()
                .boardType("board type")
                .university("university")
                .name("name")
                .description("description")
                .build();

        BoardErrorCode code = BoardErrorCode.DUPLICATE_BOARD;
        AdminManageBoardException exception =
                new AdminManageBoardException(code);
        when(boardService.createBoard(any(CreateBoardDto.class)))
                .thenThrow(exception);

        mockMvc.perform(post("/board/create")
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));
    }

    @Test
    @DisplayName("createBoards - success")
    void testCreateBoards_success() throws Exception {
        List<CreateBoardsDto> results = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            CreateBoardsDto result = CreateBoardsDto.builder()
                    .boardName("name" + i)
                    .boardType("name" + i)
                    .university("univ" + i)
                    .id(Long.valueOf(i))
                    .build();
        }

        when(boardService.createBoards()).thenReturn(results);

        mockMvc.perform(post("/board/creates"))
                .andExpect(status().isOk());

    }

}