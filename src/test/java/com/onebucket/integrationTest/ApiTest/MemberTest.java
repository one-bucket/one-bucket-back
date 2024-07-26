package com.onebucket.integrationTest.ApiTest;

import com.onebucket.domain.memberManage.dto.*;
import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import com.onebucket.global.redis.RedisRepository;
import com.onebucket.global.utils.SuccessResponseDto;
import com.onebucket.testComponent.testSupport.RestDocsSupportTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.servlet.MvcResult;

import static com.onebucket.testComponent.testUtils.JsonFieldResultMatcher.hasKey;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <br>package name   : com.onebucket.integrationTest
 * <br>file name      : MemberTest
 * <br>date           : 2024-07-23
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
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-07-23        jack8              init create
 * </pre>
 */


public class MemberTest extends RestDocsSupportTest {


    @Autowired
    private RedisRepository redisRepository;


    @Test
    @DisplayName("POST /register test")
    void register() throws Exception {
        String instantUser = "instant user";
        String instantNick = "instant";
        String instantPassword = "!3Password3!";
        CreateMemberRequestDto dto = CreateMemberRequestDto.builder()
                .username(instantUser)
                .password(instantPassword)
                .nickname(instantNick)
                .build();

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(hasKey(new SuccessResponseDto("success register")))
                .andDo(restDocs.document(
                        httpRequest(),
                        httpResponse(),
                        requestFields(
                                fieldWithPath("username").description("유저의 id 및 식별자"),
                                fieldWithPath("password")
                                        .description("유저의 비밀번호")
                                        .attributes(getFormat("하나 이상의 대문자, 소문자, 특수문자, 숫자")),
                                fieldWithPath("nickname").description("유저의 닉네임")
                        ),
                        responseFields(
                                fieldWithPath("message").description("success register")
                        )));

        String query = """
                SELECT EXISTS (
                SELECT 1
                FROM member
                WHERE username = ?
                AND nickname = ?
                ) AS user_exists;
                """;

        String isExist = jdbcTemplate.queryForObject(query, String.class, instantUser, instantNick);
        assertThat(isExist).isEqualTo("TRUE");

    }

    @Test
    @DisplayName("POST /sign-in test")
    void signIn() throws Exception {
        createInitUser();
        SignInRequestDto dto = new SignInRequestDto(testUsername, testPassword);

        TestTransaction.flagForCommit();
        TestTransaction.end();


        MvcResult result = mockMvc.perform(post("/sign-in")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        httpRequest(),
                        httpResponse(),
                        responseFields(
                                fieldWithPath("grantType").description("type of token"),
                                fieldWithPath("accessToken").description("access token"),
                                fieldWithPath("refreshToken").description("refresh token")
                        ),
                        requestFields(
                                fieldWithPath("username").description("username of account"),
                                fieldWithPath("password").description("password of account")
                        )))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        JwtToken token = objectMapper.readValue(responseContent, JwtToken.class);

        mockMvc.perform(get("/security-endpoint")
                        .header("Authorization", getAuthHeader(token)))
                .andDo(print())
                .andExpect(hasKey("This is a security endpoint!"));

    }


    @Test
    @DisplayName("GET /member/info test")
    void memberInfo() throws Exception {

        JwtToken token = createInitUser();
        ReadMemberInfoDto result = ReadMemberInfoDto.builder()
                .username(testUsername)
                .nickname(testNickname)
                .university("null")
                .build();

        mockMvc.perform(get("/member/info")
                        .header("Authorization", getAuthHeader(token))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(hasKey(result))
                .andDo(restDocs.document(
                        httpRequest(),
                        httpResponse(),
                        responseFields(
                                fieldWithPath("username").description("username of account"),
                                fieldWithPath("nickname").description("nickname of account"),
                                fieldWithPath("university").description("university of account")
                        )));

    }


    @Test
    @DisplayName("DELETE /member test")
    void memberDelete() throws Exception {

        JwtToken token = createInitUser();
        mockMvc.perform(delete("/member")
                        .header("Authorization", getAuthHeader(token)))
                .andExpect(status().isOk())
                .andExpect(hasKey(new SuccessResponseDto("success delete account")))
                .andDo(restDocs.document(
                        httpRequest(),
                        httpResponse(),
                        responseFields(
                                fieldWithPath("message").description("success delete account")
                        )
                ));

        TestTransaction.flagForCommit();
        TestTransaction.end();

        String query = """
                SELECT EXISTS (
                SELECT 1
                FROM member
                WHERE username = ?
                ) AS user_exists;
                """;

        String isExist = jdbcTemplate.queryForObject(query, String.class, testUsername);
        assertThat(isExist).isEqualTo("FALSE");

    }


    @Test
    @DisplayName("POST /password/reset test")
    void resetPassword() throws Exception {
        JwtToken token = createInitUser();

        String query = """
                SELECT password
                FROM member
                WHERE username = ?
                """;
        String oldPassword = jdbcTemplate.queryForObject(query, String.class, testUsername);


        mockMvc.perform(post("/member/password/reset")
                        .header("Authorization", getAuthHeader(token))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((hasKey(new SuccessResponseDto("success reset password"))))
                .andDo(restDocs.document(
                        httpResponse(),
                        httpRequest(),
                        responseFields(
                                fieldWithPath("message").description("success reset password")
                        )
                ));

        TestTransaction.flagForCommit();
        TestTransaction.end();


        String newPassword = jdbcTemplate.queryForObject(query, String.class, testUsername);
        assertThat(oldPassword).isNotEqualTo(newPassword);
    }


    @Test
    @DisplayName("POST /password/set test")
    void setPassword() throws Exception {
        JwtToken token = createInitUser();

        String newPassword = "!1NewPassword1!";
        SetPasswordDto dto = new SetPasswordDto(newPassword);

        String query = """
                SELECT password
                FROM member
                WHERE username = ?
                """;
        String oldSavedPassword = jdbcTemplate.queryForObject(query, String.class, testUsername);

        mockMvc.perform(post("/member/password/set")
                        .header("Authorization", getAuthHeader(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(hasKey(new SuccessResponseDto("success set password")))
                .andDo(restDocs.document(
                        httpRequest(),
                        httpResponse(),
                        requestFields(
                                fieldWithPath("password").description("new password of account").attributes(getFormat("하나 이상의 대문자, 소문자, 특수문자, 숫자"))
                        ),
                        responseFields(
                                fieldWithPath("message").description("success set password")
                        )));

        TestTransaction.flagForCommit();
        TestTransaction.end();

        String newSavedPassword = jdbcTemplate.queryForObject(query, String.class, testUsername);
        assertThat(oldSavedPassword).isNotEqualTo(newSavedPassword);



        SignInRequestDto oldPasswordSignInDto = new SignInRequestDto(testUsername, testPassword);
        SignInRequestDto newPasswordSignInDto = new SignInRequestDto(testUsername, newPassword);


        mockMvc.perform(post("/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(oldPasswordSignInDto)))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPasswordSignInDto)))
                .andExpect(status().isOk());
    }
}
//
//    @Test
//    @DisplayName("POST /nickname/set test")
//    void testSetNickname_success() throws Exception {
//        String newNickname = "newNick";
//        NicknameRequestDto dto = new NicknameRequestDto(newNickname);
//
//        mockMvc.perform(post("/member/nickname/set")
//                        .header("Authorization", getAuthHeader())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isOk())
//                .andExpect(hasKey(new SuccessResponseDto("success set nickname")))
//                .andDo(document("set-nickname",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("nickname").description("new nickname of account")
//                        ),
//                        responseFields(
//                                fieldWithPath("message").description("success set nickname")
//                        )));
//
//        TestTransaction.flagForCommit();
//        TestTransaction.end();
//
//        String query = """
//                SELECT nickname
//                FROM member
//                WHERE username = ?
//                """;
//        String savedNickname = jdbcTemplate.queryForObject(query,String.class, username);
//        assertThat(savedNickname).isEqualTo(newNickname);
//
//        String rollBackNicknameQuery = """
//                UPDATE member
//                SET nickname = ?
//                WHERE username = ?
//                """;
//
//        jdbcTemplate.update(rollBackNicknameQuery, nickname, username);
//        savedNickname = jdbcTemplate.queryForObject(query,String.class, username);
//        assertThat(savedNickname).isEqualTo(nickname);
//    }
//
//    @Test
//    @DisplayName("GET /member/{id}/nickname")
//    void testGetNickname_success() throws Exception {
//        NicknameRequestDto result = new NicknameRequestDto(nickname);
//        String getIdQuery = """
//                SELECT id
//                FROM member
//                WHERE username = ?
//                """;
//        String stringId = jdbcTemplate.queryForObject(getIdQuery, String.class, username);
//        Long id = Optional.ofNullable(stringId)
//                .map(Long::parseLong)
//                .orElse(null);
//
//        assertThat(id).isNotNull();
//
//        mockMvc.perform(RestDocumentationRequestBuilders.get("/member/{id}/nickname", id)
//                .accept(MediaType.APPLICATION_JSON)
//                .header("Authorization", getAuthHeader()))
//                .andExpect(status().isOk())
//                .andExpect(hasKey(result))
//                .andDo(document("get-nickname",
//                        preprocessResponse(prettyPrint()),
//                        pathParameters(
//                                parameterWithName("id").description("id to search user's nickname")
//                        ),
//                        responseFields(
//                                fieldWithPath("nickname").description("nickname which to find")
//                        )));
//    }
//
//    @Test
//    @DisplayName("POST /profile/update test")
//    void testProfileUpdate_success() throws Exception {
//        Long id = createInitProfile();
//
//        UpdateProfileDto dto = UpdateProfileDto.builder()
//                .name("john")
//                .birth(LocalDate.of(1999,1,1))
//                .gender("man")
//                .age(20)
//                .description("hello")
//                .build();
//
//        mockMvc.perform(post("/profile/update")
//                .header("Authorization", getAuthHeader())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(dto))
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(hasKey(new SuccessResponseDto("success update profile")))
//                .andDo(document("update-profile",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("name").description("real name of account profile"),
//                                fieldWithPath("birth").description("birthday of account profile").optional().attributes(getFormat("yyyy-mm-dd")),
//                                fieldWithPath("gender").description("gender of account profile").optional().attributes(getFormat("man or woman")),
//                                fieldWithPath("age").description("age of account profile").optional().attributes(getFormat("under 110")),
//                                fieldWithPath("description").description("description of profile").optional().attributes(getFormat("under 200 character"))
//                        ),
//                        responseFields(
//                                fieldWithPath("message").description("success update profile")
//                        )));
//        TestTransaction.flagForCommit();
//        TestTransaction.end();
//
//
//        String query = """
//                SELECT p.name, p.birth, p.gender, p.age, p.description
//                FROM profile p
//                JOIN member m ON p.id = m.id
//                WHERE m.username = ?
//                """;
//
//        UpdateProfileDto savedInfo = Optional.ofNullable(jdbcTemplate.queryForObject(query, (rs, rowNum) -> UpdateProfileDto.builder()
//                .name(rs.getString("name"))
//                .birth(rs.getDate("birth").toLocalDate())
//                .gender(rs.getString("gender"))
//                .age(rs.getInt("age"))
//                .description(rs.getString("description"))
//                .build(), username)).orElseThrow(RuntimeException::new);
//
//
//        assertThat(dto.getName()).isEqualTo(savedInfo.getName());
//        assertThat(dto.getBirth()).isEqualTo(savedInfo.getBirth());
//        assertThat(dto.getAge()).isEqualTo(savedInfo.getAge());
//        assertThat(dto.getDescription()).isEqualTo(savedInfo.getDescription());
//
//        deleteProfile(id);
//    }
//
////    @Test
////    @DisplayName("POST /profile/image")
////    void testUpdateImage() throws Exception {
////        Long id = createInitProfile();
////
////        MockMultipartFile mockFile = new MockMultipartFile(
////                "file",
////                "test-image.png",
////                "image/png",
////                "Test Image Content".getBytes()
////        );
////        mockMvc.perform(RestDocumentationRequestBuilders.multipart("/profile/image")
////                .file(mockFile)
////                .header("Authorization", getAuthHeader())
////                .contentType(MediaType.MULTIPART_FORM_DATA)
////                .accept(MediaType.APPLICATION_JSON))
////                .andDo(print())
////                .andExpect(status().isOk())
////                .andExpect(hasKey(new SuccessResponseDto("success update image")))
////                .andDo(document("update-image",
////                        preprocessRequest(prettyPrint()),
////                        preprocessResponse(prettyPrint()),
////                        requestFields(
////                                fieldWithPath("file").description("multipart file")
////                        ),
////                        responseFields(
////                                fieldWithPath("message").description("success update image")
////                        )));
////
////    }
//
//
//
//
//}
