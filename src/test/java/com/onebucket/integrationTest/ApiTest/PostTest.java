package com.onebucket.integrationTest.ApiTest;

import com.onebucket.domain.boardManage.dto.postDto.PostDto;
import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import com.onebucket.global.utils.SuccessResponseDto;
import com.onebucket.global.utils.SuccessResponseWithIdDto;
import com.onebucket.testComponent.testSupport.BoardRestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static com.onebucket.testComponent.testUtils.JsonFieldResultMatcher.hasKey;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <br>package name   : com.onebucket.integrationTest.ApiTest
 * <br>file name      : PostTest
 * <br>date           : 10/11/24
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
public class PostTest extends BoardRestDocsSupport {

    // POST TEST

    @Test
    @DisplayName("GET /post/list/{boardId}")
    void getPostsByBoard() throws Exception {
        JwtToken jwtToken = createInitUser();
        Long userId = stackId - 1;

        Long univId = createUniversity(1);
        Long boardTypeId = createBoardType(1);
        Long boardId = createBoard(univId, boardTypeId);

        setUniversityToUser(userId, univId);

        Long postId = createPost(5, boardId, userId);

        List<PostDto.Thumbnail> listResults = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            PostDto.Thumbnail result = PostDto.Thumbnail.builder()
                    .postId(postId + i)
                    .title("title" + (postId + i))
                    .text("text" + (postId + i))
                    .authorNickname(testNickname)
                    .boardId(boardId)
                    .build();

            listResults.add(result);
        }

        Page<PostDto.Thumbnail> results = toPage(listResults);



        mockMvc.perform(RestDocumentationRequestBuilders.get("/post/list/{boardId}", boardId)
                .header("Authorization", getAuthHeader(jwtToken))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(hasKey(results))
                .andDo(restDocs.document(
                        httpRequest(),
                        pathParameters(
                                parameterWithName("boardId").description("The ID of the board to search")
                        ),
                        httpResponse(),
                        responseFields(
                                // content 배열 안의 객체 필드들에 대한 경로
                                fieldWithPath("content[].boardId").description("The ID of the board to which the post belongs"),
                                fieldWithPath("content[].title").description("The title of the post"),
                                fieldWithPath("content[].text").description("The content of the post"),
                                fieldWithPath("content[].postId").description("The unique ID of the post"),
                                fieldWithPath("content[].authorNickname").description("The nickname of the author who wrote the post"),
                                fieldWithPath("content[].createdDate").description("The creation date and time of the post"),
                                fieldWithPath("content[].modifiedDate").description("The last modified date and time of the post"),
                                fieldWithPath("content[].views").description("The number of times the post has been viewed"),
                                fieldWithPath("content[].likes").description("The number of likes the post has received"),
                                fieldWithPath("content[].commentsCount").description("The number of comments on the post"),
                                fieldWithPath("content[].imageUrls").description("A list of URLs for images included in the post")

                        ).andWithPrefix("", getPageCommonFields())
                ));
    }

    @Test
    @DisplayName("GET /post/{postId}")
    void getPostsById() throws Exception {

        JwtToken token = createInitUser();
        Long userId = stackId - 1;

        Long univId = createUniversity(1);
        setUniversityToUser(userId, univId);

        Long boardTypeId = createBoardType(1);
        Long boardId = createBoard(univId, boardTypeId);

        Long postId = createPost(1, boardId, userId);

        PostDto.ResponseInfo result = PostDto.ResponseInfo.builder()
                .postId(postId)
                .boardId(boardId)
                .authorNickname(testNickname)
                .title("title" + postId)
                .text("text" + postId)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/post/{postId}", postId)
                .header("Authorization", getAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(hasKey(result))
                .andDo(restDocs.document(
                        httpRequest(),
                        pathParameters(
                                parameterWithName("postId").description("The ID of the post to which to search")
                        ),
                        httpResponse(),
                        responseFields(
                                // content 배열 안의 객체 필드들에 대한 경로
                                fieldWithPath("boardId").description("The ID of the board to which the post belongs"),
                                fieldWithPath("title").description("The title of the post"),
                                fieldWithPath("text").description("The content of the post"),
                                fieldWithPath("postId").description("The unique ID of the post"),
                                fieldWithPath("authorNickname").description("The nickname of the author who wrote the post"),
                                fieldWithPath("createdDate").description("The creation date and time of the post"),
                                fieldWithPath("modifiedDate").description("The last modified date and time of the post"),
                                fieldWithPath("views").description("The number of times the post has been viewed"),
                                fieldWithPath("likes").description("The number of likes the post has received"),
                                fieldWithPath("commentsCount").description("The number of comments on the post"),
                                fieldWithPath("imageUrls").description("A list of URLs for images included in the post"),
                                fieldWithPath("comments").description("A list of comments on the post"),
                                fieldWithPath("userAlreadyLikes").description("Whether the user has already liked the post")
                        )
                ));
    }

    @Test
    @DisplayName("DELETE /post/{postId}")
    void deletePost() throws Exception {
        JwtToken token = createInitUser();
        Long userId = stackId - 1;

        Long univId = createUniversity(1);
        setUniversityToUser(userId, univId);

        Long boardTypeId = createBoardType(1);
        Long boardId = createBoard(univId, boardTypeId);

        Long postId = createPost(1, boardId, userId);

        String query = """
                SELECT EXISTS(
                    SELECT 1
                    FROM post
                    WHERE title = ?)
                """;

        boolean isExist = Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, boolean.class, "title" + postId));

        assertThat(isExist).isEqualTo(true);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/post/{postId}", postId)
                .header("Authorization", getAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(hasKey(new SuccessResponseWithIdDto("success delete post", postId)))
                .andDo(restDocs.document(
                        httpRequest(),
                        pathParameters(
                                parameterWithName("postId").description("postId which to delete")),
                        httpResponse(),
                        responseFields(
                                fieldWithPath("message").description("success delete post"),
                                fieldWithPath("id").description("postId that deleted")
                        )
                ));

        isExist = Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, boolean.class, "title " + postId));
        assertThat(isExist).isEqualTo(false);
    }

    @Test
    @DisplayName("POST /post/{postId}/like")
    void addLikes() throws Exception {
        JwtToken token = createInitUser();
        Long userId = stackId - 1;
        Long univId = createUniversity(1);
        setUniversityToUser(userId, univId);

        Long boardTypeId = createBoardType(1);
        Long boardId = createBoard(univId, boardTypeId);
        Long postId = createPost(1, boardId, userId);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/post/{postId}/like", postId)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", getAuthHeader(token)))
                .andExpect(status().isOk())
                .andExpect(hasKey(new SuccessResponseDto("success add likes")))
                .andDo(restDocs.document(
                        httpRequest(),
                        pathParameters(
                                parameterWithName("postId").description("postId which to add likes")
                        ),
                        httpResponse(),
                        responseFields(
                                fieldWithPath("message").description("success add likes")
                        )
                ));

        String query = """
                SELECT EXISTS (
                    SELECT 1
                    FROM likes_map
                    WHERE member_id = ? AND post_id = ?)
                """;
        Boolean isLikeExist = jdbcTemplate.queryForObject(query, Boolean.class, userId, postId);

        assertThat(isLikeExist).isEqualTo(true);
    }

    @Test
    @DisplayName("DELETE /post/{postId}/like")
    void deleteLikes() throws Exception {
        JwtToken token = createInitUser();
        Long userId = stackId - 1;
        Long univId = createUniversity(1);
        setUniversityToUser(userId, univId);

        Long boardTypeId = createBoardType(1);
        Long boardId = createBoard(univId, boardTypeId);
        Long postId = createPost(1, boardId, userId);

        String addLikeQuery = """
                
                """;



        mockMvc.perform(RestDocumentationRequestBuilders.delete("/post/{postId}/like", postId)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", getAuthHeader(token)))
                .andExpect(status().isOk())
                .andExpect(hasKey(new SuccessResponseDto("success delete likes")))
                .andDo(restDocs.document(
                        httpRequest(),
                        pathParameters(
                                parameterWithName("postId").description("postId which to delete likes")
                        ),
                        httpResponse(),
                        responseFields(
                                fieldWithPath("message").description("success delete likes")
                        )

                ));




    }




}
