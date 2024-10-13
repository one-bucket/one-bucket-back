package com.onebucket.integrationTest.ApiTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.onebucket.domain.universityManage.dto.verifiedCode.request.RequestCodeCheckDto;
import com.onebucket.domain.universityManage.dto.verifiedCode.request.RequestCodeDto;
import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import com.onebucket.global.utils.SuccessResponseDto;
import com.onebucket.testComponent.testSupport.RestDocsSupportTest;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.jdbc.Sql;


import java.util.Properties;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;
import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.onebucket.testComponent.testUtils.JsonFieldResultMatcher.hasKey;
/**
 * <br>package name   : com.onebucket.integrationTest.ApiTest
 * <br>file name      : MailTest
 * <br>date           : 2024-10-13
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
 * 2024-10-13        SeungHoon              init create
 * </pre>
 */
@Import(MailTest.TestMailConfig.class)
public class MailTest extends RestDocsSupportTest {
    @BeforeEach
    public void setUp() {
        greenMail.start();
    }
    @Test
    @DisplayName("POST / UniversityEmail sendVerifiedCode")
    @Sql(scripts = "/sql/InitDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void sendVerifiedCode_and_verifyCode() throws Exception {
        JwtToken token = createInitUser();
        createUniversity();
        RequestCodeDto requestCodeDto = new RequestCodeDto("홍익대학교","example@hongik.ac.kr");
        String getIdQuery = """
                SELECT id
                FROM member
                WHERE username = ?
                """;
        Long id = jdbcTemplate.queryForObject(getIdQuery, Long.class, testUsername);
        assertThat(id).isNotNull();

        String query = """
                SELECT count(*)
                FROM member_roles
                WHERE member_id = ?
                """;
        Integer BeforeRoleCount = jdbcTemplate.queryForObject(query, Integer.class, id);
        assertThat(BeforeRoleCount).isEqualTo(1);

        mockMvc.perform(post("/univ/send-code")
                .header("Authorization",getAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestCodeDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(hasKey(new SuccessResponseDto("success send verifiedCode")))
                .andDo(restDocs.document(
                        httpRequest(),
                        httpResponse(),
                        requestFields(
                                fieldWithPath("university").description("재학 중인 대학교 이름"),
                                fieldWithPath("universityEmail").description("재학 중인 대학교 이메일")
                                        .attributes(getFormat("이메일 형식을 갖추어야 한다."))
                        ),
                        responseFields(
                                fieldWithPath("message").description("success send verifiedCode")
                        )
                ));

        // GreenMail을 사용하여 이메일이 발송되었는지 확인
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertThat(receivedMessages).hasSize(1);
        assertThat(receivedMessages[0].getSubject()).isEqualTo("[한바구니] 학교 이메일 인증");

//        Object content = receivedMessages[0].getContent();
//        Multipart multipart = (Multipart) content;
//        System.out.println(multipart);
    }

    private void createUniversity() {
        Long id = 1L;
        String address = "address";
        String email = "hongik.ac.kr";
        String name = "홍익대학교";
        String query = """
                INSERT INTO university (id, address, email, name)
                VALUES (?, ?, ?, ?)
                """;
        jdbcTemplate.update(query, id, address, email, name);
    }

    @TestConfiguration
    static class TestMailConfig {
        @Bean
        public JavaMailSender mailSender() {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("localhost");
            mailSender.setPort(greenMail.getSmtp().getPort()); // GreenMail에서 할당된 포트를 사용
            mailSender.setUsername("dummy");
            mailSender.setPassword("dummy");
            mailSender.setProtocol("smtp");

            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", true);
            props.put("mail.smtp.starttls.enable", true);

            return mailSender;
        }
    }

}
