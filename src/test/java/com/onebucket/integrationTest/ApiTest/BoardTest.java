package com.onebucket.integrationTest.ApiTest;

import com.onebucket.domain.boardManage.dto.request.RequestCreateBoardDto;
import com.onebucket.domain.boardManage.dto.request.RequestCreateBoardTypeDto;
import com.onebucket.domain.boardManage.dto.response.ResponseBoardIdAndNameDto;
import com.onebucket.domain.boardManage.dto.response.ResponseCreateBoardsDto;
import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import com.onebucket.global.utils.SuccessResponseDto;
import com.onebucket.testComponent.testSupport.RestDocsSupportTest;
import com.onebucket.testComponent.testSupport.UserRestDocsSupportTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
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
public class BoardTest extends UserRestDocsSupportTest {

    @Test
    @DisplayName("POST /admin/board/create test")
    @Sql(scripts = "/sql/InitDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBoard() throws Exception {
        JwtToken jwtToken = createInitUser();
        createBoardType(1L);
        createUniversity(1L);

        RequestCreateBoardDto dto = RequestCreateBoardDto.builder()
                .boardType("type1")
                .description("description")
                .name("board")
                .university("univ1")
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

    @Test
    @DisplayName("POST /admin/board/creates test")
    @Sql(scripts = "/sql/InitDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void boardCreates() throws Exception {
        JwtToken token = createInitUser();
        List<ResponseCreateBoardsDto> results = new ArrayList<>();
        for(long i = 1L; i <= 3L; i++) {
            createBoardType(i);
            createUniversity(i);
            for(long j = 1L; j <= 3L; j++) {
                ResponseCreateBoardsDto result = ResponseCreateBoardsDto.builder()
                        .university("univ" + i)
                        .boardType("type" + j)
                        .build();
                results.add(result);
            }
        }


        mockMvc.perform(post("/board/creates")
                .header("Authorization", getAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(hasKey(results))
                .andDo(restDocs.document(
                        httpRequest(),
                        httpResponse(),
                        responseFields(
                                fieldWithPath("[].id").description("id which saved automatically"),
                                fieldWithPath("[].boardName").description("name of board"),
                                fieldWithPath("[].university").description("name of university of saved board"),
                                fieldWithPath("[].boardType").description("name of boardType of saved board")
                        )
                ));

        String query = """
                SELECT COUNT(*)
                FROM board
                """;
        Long countOfBoard = jdbcTemplate.queryForObject(query, Long.class);
        assertThat(countOfBoard).isEqualTo(9L);
    }

    @Test
    @DisplayName("POST /board/type test")
    @Sql(scripts = "/sql/InitDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void boardTypeCreates() throws Exception {
        JwtToken token = createInitUser();

        RequestCreateBoardTypeDto dto = RequestCreateBoardTypeDto.builder()
                .description("description")
                .name("type")
                .type("Post")
                .build();

        mockMvc.perform(post("/board/type")
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

    @Test
    @DisplayName("POST /board/list test")
    @Sql(scripts = "/sql/InitDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBoardList() throws Exception {
        JwtToken token = createInitUser();
        createBoard(5);

        String setUnivQuery = """
                UPDATE member
                SET university_id = 1
                WHERE username = ?
                """;
        jdbcTemplate.update(setUnivQuery, testUsername);


        List<ResponseBoardIdAndNameDto> results = new ArrayList<>();
        long index = 1;
        for(long i = 1; i <= 5; i++) {
                ResponseBoardIdAndNameDto result = ResponseBoardIdAndNameDto.builder()
                    .id(index)
                    .name("board1" + i)
                    .build();
            results.add(result);
            index++;
        }

        mockMvc.perform(get("/board/list")
                        .header("Authorization", getAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(hasKey(results))
                .andDo(restDocs.document(
                        httpRequest(),
                        httpResponse(),
                        responseFields(
                                fieldWithPath("[].id").description("id of board"),
                                fieldWithPath("[].name").description("name of board"),
                                fieldWithPath("[].type").description("type of board")
                        )
                ));
    }


    private void createBoardType(Long id) {
        String description = "description" + id;
        String name = "type" + id;
        String query = """
                INSERT INTO board_type (id, description, name)
                VALUES (?, ?, ?)
                """;
        jdbcTemplate.update(query, id, description, name);
    }

    private Long createUniversity() {

        String address = "address" + stackUnivId;
        String email = "email@email." + stackUnivId;
        String name = "univ" + stackUnivId;
        String query = """
                INSERT INTO university (id, address, email, name)
                VALUES (?, ?, ?, ?)
                """;
        jdbcTemplate.update(query, stackUnivId, address, email, name);
    }


}
