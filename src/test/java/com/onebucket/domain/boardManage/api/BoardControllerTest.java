package com.onebucket.domain.boardManage.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.onebucket.domain.boardManage.dto.internal.board.BoardIdAndNameDto;
import com.onebucket.domain.boardManage.service.BoardService;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.universityManage.domain.University;
import com.onebucket.global.exceptionManage.customException.universityManageException.UniversityException;
import com.onebucket.global.exceptionManage.errorCode.UniversityErrorCode;
import com.onebucket.global.exceptionManage.exceptionHandler.BaseExceptionHandler;
import com.onebucket.global.exceptionManage.exceptionHandler.DataExceptionHandler;
import com.onebucket.global.utils.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.onebucket.testComponent.testUtils.JsonFieldResultMatcher.hasKey;
import static com.onebucket.testComponent.testUtils.JsonFieldResultMatcher.hasStatus;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * <br>package name   : com.onebucket.domain.boardManage.api
 * <br>file name      : BoardControllerTest
 * <br>date           : 2024-09-09
 * <pre>
 * <span style="color: white;">[description]</span>
 * {@link BoardController} 에 대한 테스트코드이다.
 * </pre>
 */

@ExtendWith(MockitoExtension.class)
class BoardControllerTest {

    @Mock
    private BoardService boardService;
    @Mock
    private MemberService memberService;
    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private University mockUniversity;

    @InjectMocks
    private BoardController boardController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        RequestBuilder defaultBuilder = get("/")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc = MockMvcBuilders.standaloneSetup(boardController)
                .setControllerAdvice(new BaseExceptionHandler(), new DataExceptionHandler())
                .defaultRequest(defaultBuilder)
                .build();
    }

    //+-+-+-+-+-+-+-+-]] getBoardList [[+-+-+-+-+-+-+-+-
    @Test
    @DisplayName("getBoardList - success")
    void testGetBoardList_success() throws Exception {

        List<BoardIdAndNameDto> boardIdAndNameDtoList = new ArrayList<>();
        for(long i = 1L; i <= 10L; i++) {
            BoardIdAndNameDto boardIdAndNameDto = BoardIdAndNameDto.builder()
                    .id(i)
                    .name("name" + i)
                    .build();

            boardIdAndNameDtoList.add(boardIdAndNameDto);
        }
        when(securityUtils.getUnivId()).thenReturn(anyLong());
        when(boardService.getBoardList(1L)).thenReturn(boardIdAndNameDtoList);

        mockMvc.perform(get("/board/list"))
                .andExpect(status().isOk())
                .andExpect(hasKey(boardIdAndNameDtoList))
                .andExpect(jsonPath("$.length()").value(10));
    }

    @Test
    @DisplayName("getBoardList - success / but no board exist")
    void testGetBoardList_success_noBoardReturn() throws Exception {
        when(securityUtils.getUnivId()).thenReturn(anyLong());
        when(boardService.getBoardList(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/board/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("getBoardList - fail / invalid university return")
    void testGetBoardList_fail_invalidUniv() throws Exception {
        UniversityErrorCode code = UniversityErrorCode.NOT_EXIST_UNIVERSITY;
        UniversityException exception = new UniversityException(code);

        when(securityUtils.getUnivId()).thenThrow(exception);

        mockMvc.perform(get("/board/list"))
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));

        verify(boardService, never()).getBoardList(anyLong());
    }
}