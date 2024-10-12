package com.onebucket.integrationTest.ApiTest;

import com.onebucket.domain.memberManage.domain.Profile;
import com.onebucket.domain.memberManage.dto.*;
import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import com.onebucket.global.minio.MinioSaveInfoDto;
import com.onebucket.global.utils.SuccessResponseDto;
import com.onebucket.testComponent.testSupport.RestDocsSupportTest;
import com.onebucket.testComponent.testSupport.UserRestDocsSupportTest;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Optional;

import static com.onebucket.testComponent.testUtils.JsonFieldResultMatcher.hasKey;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <br>package name   : com.onebucket.integrationTest
 * <br>file name      : MemberTest
 * <br>date           : 2024-07-23
 * <pre>
 * <span style="color: white;">[description]</span>
 * MemberManage 패키지에 명시된 사용자 관리에 대한 통합 API 테스트
 * </pre>
 */


public class MemberTest extends UserRestDocsSupportTest {



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

        deleteRedisUser(testUsername);

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


//    @Test
//    @DisplayName("POST /password/reset test")
//    void resetPassword() throws Exception {
//        JwtToken token = createInitUser();
//        String query = """
//                SELECT password
//                FROM member
//                WHERE username = ?
//                """;
//        String oldPassword = jdbcTemplate.queryForObject(query, String.class, testUsername);
//
//
//        mockMvc.perform(post("/member/password/reset")
//                        .header("Authorization", getAuthHeader(token))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect((hasKey(new SuccessResponseDto("success reset password"))))
//                .andDo(restDocs.document(
//                        httpResponse(),
//                        httpRequest(),
//                        responseFields(
//                                fieldWithPath("message").description("success reset password")
//                        )
//                ));
//
//
//
//        String newPassword = jdbcTemplate.queryForObject(query, String.class, testUsername);
//        assertThat(oldPassword).isNotEqualTo(newPassword);
//    }


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

        deleteRedisUser(testUsername);
    }


    @Test
    @DisplayName("POST /nickname/set test")
    void setNickname() throws Exception {

        JwtToken token = createInitUser();
        String newNickname = "newNick";
        NicknameRequestDto dto = new NicknameRequestDto(newNickname);

        mockMvc.perform(post("/member/nickname/set")
                        .header("Authorization", getAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(hasKey(new SuccessResponseDto("success set nickname")))
                .andDo(restDocs.document(
                        httpRequest(),
                        httpResponse(),
                        requestFields(
                                fieldWithPath("nickname").description("new nickname of account")
                        ),
                        responseFields(
                                fieldWithPath("message").description("success set nickname")
                        )));


        String query = """
                SELECT nickname
                FROM member
                WHERE username = ?
                """;
        String savedNickname = jdbcTemplate.queryForObject(query,String.class, testUsername);
        assertThat(savedNickname).isEqualTo(newNickname);

    }


    @Test
    @DisplayName("GET /member/{id}/nickname")
    void getNickname() throws Exception {
        JwtToken token = createInitUser();
        NicknameRequestDto result = new NicknameRequestDto(testNickname);
        String getIdQuery = """
                SELECT id
                FROM member
                WHERE username = ?
                """;
        Long id  = Optional.ofNullable(jdbcTemplate.queryForObject(getIdQuery, String.class, testUsername))
                .map(Long::parseLong)
                .orElse(null);

        assertThat(id).isNotNull();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/member/{id}/nickname", id)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", getAuthHeader(token)))
                .andExpect(status().isOk())
                .andExpect(hasKey(result))
                .andDo(restDocs.document(
                        httpRequest(),
                        httpResponse(),
                        pathParameters(
                                parameterWithName("id").description("id to search user's nickname")
                        ),
                        responseFields(
                                fieldWithPath("nickname").description("nickname which to find")
                        )));
    }

    @Test
    @DisplayName("POST /profile/update test")
    void updateProfile() throws Exception {
        JwtToken token = createInitUser();
        Long id = createInitProfile();

        UpdateProfileDto dto = UpdateProfileDto.builder()
                .name("john")
                .birth(LocalDate.of(1999,1,1))
                .gender("man")
                .age(20)
                .description("hello")
                .build();

        mockMvc.perform(post("/profile/update")
                .header("Authorization", getAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(hasKey(new SuccessResponseDto("success update profile")))
                .andDo(restDocs.document(
                        httpRequest(),
                        httpResponse(),
                        requestFields(
                                fieldWithPath("name").description("real name of account profile"),
                                fieldWithPath("birth").description("birthday of account profile").optional().attributes(getFormat("yyyy-mm-dd")),
                                fieldWithPath("gender").description("gender of account profile").optional().attributes(getFormat("man or woman")),
                                fieldWithPath("age").description("age of account profile").optional().attributes(getFormat("under 110")),
                                fieldWithPath("description").description("description of profile").optional().attributes(getFormat("under 200 character"))
                        ),
                        responseFields(
                                fieldWithPath("message").description("success update profile")
                        )));


        String query = """
                SELECT p.name, p.birth, p.gender, p.age, p.description
                FROM profile p
                JOIN member m ON p.id = m.id
                WHERE m.username = ?
                """;

        UpdateProfileDto savedInfo = Optional.ofNullable(jdbcTemplate.queryForObject(query, (rs, rowNum) -> UpdateProfileDto.builder()
                .name(rs.getString("name"))
                .birth(rs.getDate("birth").toLocalDate())
                .gender(rs.getString("gender"))
                .age(rs.getInt("age"))
                .description(rs.getString("description"))
                .build(), testUsername)).orElseThrow(RuntimeException::new);


        assertThat(dto.getName()).isEqualTo(savedInfo.getName());
        assertThat(dto.getBirth()).isEqualTo(savedInfo.getBirth());
        assertThat(dto.getAge()).isEqualTo(savedInfo.getAge());
        assertThat(dto.getDescription()).isEqualTo(savedInfo.getDescription());
    }

    @Test
    @DisplayName("POST /profile/image")
    void updateImage() throws Exception {
        JwtToken token = createInitUser();
        Long id = createInitProfile();

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test-image.png",
                "image/png",
                "Test Image Content".getBytes()
        );
        mockMvc.perform(RestDocumentationRequestBuilders.multipart("/profile/image")
                        .file(mockFile)
                        .header("Authorization", getAuthHeader(token))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(hasKey(new SuccessResponseDto("success update image")))
                .andDo(print())
                .andDo(restDocs.document(
                        httpRequest(),
                        httpResponse(),
                        requestParts(
                                partWithName("file").description("multipart file")
                        ),
                        responseFields(
                                fieldWithPath("message").description("success update image")
                        )));

        MinioSaveInfoDto saveInfoDto = MinioSaveInfoDto.builder()
                .fileExtension("png")
                .fileName("/profile/" + id + "/profile_image")
                .bucketName(bucketName)
                .build();
        byte[] savedImage = minioRepository.getFile(saveInfoDto);

        assertThat(savedImage).isNotNull();
        assertThat(savedImage.length).isEqualTo(mockFile.getBytes().length);
        assertThat(savedImage).isEqualTo(mockFile.getBytes());

        minioRepository.deleteFile(saveInfoDto);

    }

    @Test
    @DisplayName("GET /profile/image")
    void getImage() throws Exception {
         JwtToken token = createInitUser();
         Long id = createInitProfile();

         MockMultipartFile mockFile = new MockMultipartFile(
                 "file",
                 "test-image.png",
                 "image/png",
                 "Test Image Content".getBytes()
         );

         MinioSaveInfoDto saveInfoDto = MinioSaveInfoDto.builder()
                 .fileExtension("png")
                 .fileName("/profile/" + id + "/profile_image")
                 .bucketName(bucketName)
                 .build();

         minioRepository.uploadFile(mockFile, saveInfoDto);

         String query = """
                 UPDATE profile
                 SET is_basic_image = false
                 WHERE id = ?
                 """;
         jdbcTemplate.update(query, id);

         mockMvc.perform(RestDocumentationRequestBuilders.get("/profile/image")
                 .header("Authorization", getAuthHeader(token))
                 .accept(MediaType.IMAGE_PNG_VALUE))
                 .andExpect(status().isOk())
                 .andExpect(content().bytes(mockFile.getBytes()))
                 .andDo(restDocs.document(
                         httpRequest(),
                         httpResponse()
                 ));

         minioRepository.deleteFile(saveInfoDto);
    }

    @Test
    @DisplayName("POST /profile/basic-image")
    void updateBasicImage() throws Exception {
        JwtToken token = createInitUser();
        Long id = createInitProfile();

        String queryToUpdate = """
                UPDATE profile
                SET is_basic_image = FALSE
                WHERE id = ?
                """;
        jdbcTemplate.update(queryToUpdate, id);

        mockMvc.perform(post("/profile/basic-image")
                .header("Authorization", getAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(hasKey(new SuccessResponseDto("success update basic image")))
                .andDo(restDocs.document(
                        httpRequest(),
                        httpResponse(),
                        responseFields(
                                fieldWithPath("message").description("success update basic image")
                        )
                ));
        String queryToFind = """
                SELECT is_basic_image
                FROM profile
                WHERE id = ?
                """;

        String isBasicImage = jdbcTemplate.queryForObject(queryToFind, String.class, id);
        assertThat(isBasicImage).isEqualTo("TRUE");
    }

    @Test
    @DisplayName("GET /profile")
    void getProfile() throws Exception {
        JwtToken token = createInitUser();
        Long id = createInitProfile();




        MvcResult result = mockMvc.perform(get("/profile")
                        .header("Authorization", getAuthHeader(token))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        httpRequest(),
                        httpResponse(),
                        responseFields(
                                fieldWithPath("name")
                                        .description("name of account profile"),
                                fieldWithPath("gender")
                                        .description("gender of account profile"),
                                fieldWithPath("email")
                                        .description("email of account email"),
                                fieldWithPath("age")
                                        .description("age of account profile"),
                                fieldWithPath("birth")
                                        .description("birth of account profile"),
                                fieldWithPath("description")
                                        .description("description of account profile"),
                                fieldWithPath("createAt")
                                        .description("create date when profile create"),
                                fieldWithPath("updateAt")
                                        .description("modified date when profile modified")
                        )))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        Profile response = objectMapper.readValue(responseContent, Profile.class);

        assertThat(response.getName()).isEqualTo(initProfileInfo.getName());
        assertThat(response.getBirth()).isEqualTo(initProfileInfo.getBirth());
        assertThat(response.getAge()).isEqualTo(initProfileInfo.getAge());


    }




}
