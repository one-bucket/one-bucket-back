package com.onebucket.integrationTest.ApiTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.onebucket.domain.memberManage.dto.CreateMemberRequestDto;
import com.onebucket.domain.memberManage.dto.SignInRequestDto;
import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import com.onebucket.global.redis.RedisRepository;
import com.onebucket.global.utils.SuccessResponseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.nio.charset.StandardCharsets;

import static com.onebucket.testComponent.testUtils.JsonFieldResultMatcher.hasKey;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class MemberTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisRepository redisRepository;
    private ObjectMapper objectMapper;
    private static final String username = "test-user";
    private static JwtToken jwtToken;


    private String getAuthHeader() {
        return jwtToken.getGrantType() + " " + jwtToken.getAccessToken();
    }

    @BeforeAll
    static void beforeAll(@Autowired MockMvc mockMvc) throws Exception{SignInRequestDto signInRequestDto = new SignInRequestDto(username, "!1password1!");
        mockMvc.perform(post("/test/create-testuser")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(new ObjectMapper().writeValueAsString(signInRequestDto)))
                .andExpect(status().isOk());


        MvcResult result = mockMvc.perform(post("/sign-in")
               .contentType(MediaType.APPLICATION_JSON)
               .content(new ObjectMapper().writeValueAsString(signInRequestDto)))
               .andExpect(status().isOk())
               .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        jwtToken = new ObjectMapper().readValue(responseContent, JwtToken.class);
    }

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }


    @Test
    @DisplayName("register,sign-in - success")
    void testRegister_success() throws Exception {
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
                .andExpect(hasKey(new SuccessResponseDto("success register")));

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

        //-+-+-+-+-+  sign-in  -+-+-+-+-+

        SignInRequestDto signInDto = new SignInRequestDto(instantUser, instantPassword);
        MvcResult signInResult = mockMvc.perform(post("/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInDto)))
                .andExpect(status().isOk())
                .andReturn();

        String signInResponse = signInResult.getResponse().getContentAsString();
        JwtToken instantToken  = new ObjectMapper().readValue(signInResponse, JwtToken.class);

        assertThat(instantToken.getGrantType()).isEqualTo("Bearer");
        assertThat(instantToken.getAccessToken()).isNotNull();
        assertThat(instantToken.getRefreshToken()).isNotNull();

        String refreshTokenKey = "refreshToken:" + instantUser;
        String saveRefreshToken = redisRepository.get(refreshTokenKey);

        assertThat(saveRefreshToken).isEqualTo(instantToken.getRefreshToken());

        redisRepository.delete(refreshTokenKey);
    }



    @Test
    @DisplayName("resetPassword - success")
    void testResetPassword_success() throws Exception {
        String query = """
                SELECT password
                FROM member
                WHERE username = ?
                """;
        String oldPassword = jdbcTemplate.queryForObject(query, String.class, username);
        mockMvc.perform(post("/member/password/reset")
                        .header("Authorization", getAuthHeader())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect((hasKey(new SuccessResponseDto("success reset password"))));

        String newPassword = jdbcTemplate.queryForObject(query, String.class, username);
        assertThat(newPassword).isNotEqualTo(oldPassword);

    }

}
