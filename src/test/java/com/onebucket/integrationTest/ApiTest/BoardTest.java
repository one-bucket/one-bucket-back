package com.onebucket.integrationTest.ApiTest;

import com.onebucket.domain.boardManage.dto.request.RequestCreateBoardDto;
import com.onebucket.domain.boardManage.dto.request.RequestCreateBoardTypeDto;
import com.onebucket.domain.boardManage.dto.response.ResponseBoardIdAndNameDto;
import com.onebucket.domain.boardManage.dto.response.ResponseCreateBoardsDto;
import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import com.onebucket.global.utils.SuccessResponseDto;
import com.onebucket.testComponent.testSupport.BoardRestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static com.onebucket.testComponent.testUtils.JsonFieldResultMatcher.hasKey;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <br>package name   : com.onebucket.integrationTest.ApiTest
 * <br>file name      : BoardTest
 * <br>date           : 2024-09-15
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 */
@Sql(scripts = "/sql/InitDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BoardTest extends BoardRestDocsSupport {

    @Test
    @DisplayName("POST /admin/board/create test")
    void boardCreate() throws Exception {
        JwtToken jwtToken = createInitUser();
        Long boardTypeId = createBoardType(1);
        Long univId = createUniversity(1);

        RequestCreateBoardDto dto = RequestCreateBoardDto.builder()
                .boardType("type" + boardTypeId)
                .description("description")
                .name("board")
                .university("univ" + univId)
                .build();

        mockMvc.perform(post("/admin/board/create")
                .header("Authorization", getAuthHeader(jwtToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(hasKey("message", "success create board"))
                .andDo(restDocs.document(
                        httpRequest(),
                        httpResponse(),
                        requestFields(
                                fieldWithPath("boardType").description("name of board type that saved before"),
                                fieldWithPath("name").description("name of board to save"),
                                fieldWithPath("university").description("name of university that saved before"),
                                fieldWithPath("description").description("description of board to save")
                        ),
                        responseFields(
                                fieldWithPath("message").description("success create board"),
                                fieldWithPath("id").description("id that saved")
                        )
                ));

        String query = """
                SELECT count(*)
                FROM board
                """;
        Long countOfBoard = jdbcTemplate.queryForObject(query, Long.class);

        assertThat(countOfBoard).isEqualTo(1L);
    }

//    @Test
//    @DisplayName("POST /admin/board/creates test")
//    void boardCreates() throws Exception {
//        JwtToken token = createInitUser();
//        List<ResponseCreateBoardsDto> results = new ArrayList<>();
//
//        Long startBoardTypeId = createBoardType(3);
//        Long startUnivId = createUniversity(3);
//
//        for(int i = 0; i < 3; i++) {
//            for(int j = 0; j < 3; j++) {
//                ResponseCreateBoardsDto result = ResponseCreateBoardsDto.builder()
//                        .university("univ" + (startUnivId + i))
//                        .boardType("type" + (startBoardTypeId + j))
//                        .build();
//                results.add(result);
//            }
//        }
//
//        mockMvc.perform(post("/admin/board/creates")
//                .header("Authorization", getAuthHeader(token))
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(hasKey(results))
//                .andDo(restDocs.document(
//                        httpRequest(),
//                        httpResponse(),
//                        responseFields(
//                                fieldWithPath("[].id").description("id which saved automatically"),
//                                fieldWithPath("[].boardName").description("name of board"),
//                                fieldWithPath("[].university").description("name of university of saved board"),
//                                fieldWithPath("[].boardType").description("name of boardType of saved board")
//                        )
//                ));
//
//        String query = """
//                SELECT COUNT(*)
//                FROM board
//                """;
//        Long countOfBoard = jdbcTemplate.queryForObject(query, Long.class);
//        assertThat(countOfBoard).isEqualTo(9L);
//    }

    @Test
    @DisplayName("POST /admin/board/type test")
    @Sql(scripts = "/sql/InitDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void boardTypeCreates() throws Exception {
        JwtToken token = createInitUser();

        RequestCreateBoardTypeDto dto = RequestCreateBoardTypeDto.builder()
                .description("description")
                .name("type")
                .type("Post")
                .build();

        mockMvc.perform(post("/admin/board/type")
                .header("Authorization", getAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(hasKey(new SuccessResponseDto("success create board type")))
                .andDo(restDocs.document(
                        httpRequest(),
                        httpResponse(),
                        requestFields(
                                fieldWithPath("name").description("name of board type"),
                                fieldWithPath("description").description("description of board type"),
                                fieldWithPath("type").description("type which is post or market post or else")
                        ),
                        responseFields(
                                fieldWithPath("message").description("success create board type")
                        )
                ));

        String query = """
                SELECT COUNT(*)
                FROM board_type
                """;
        Long countOfBoardType = jdbcTemplate.queryForObject(query, Long.class);

        assertThat(countOfBoardType).isEqualTo(1L);

    }


}
