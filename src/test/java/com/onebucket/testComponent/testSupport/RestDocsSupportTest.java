package com.onebucket.testComponent.testSupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.onebucket.domain.mailManage.service.MailService;
import com.onebucket.domain.mailManage.service.MailServiceImpl;
import org.junit.jupiter.api.extension.RegisterExtension;
import com.onebucket.domain.memberManage.dto.UpdateProfileDto;
import com.onebucket.global.auth.config.SecurityConfig;
import com.onebucket.global.auth.jwtAuth.component.JwtProvider;
import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import com.onebucket.global.minio.MinioRepository;
import com.onebucket.global.redis.RedisRepository;
import com.onebucket.testComponent.testUtils.RestDocsConfiguration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.restdocs.snippet.Attributes.key;

/**
 * <br>package name   : com.onebucket.testComponent.testSupport
 * <br>file name      : RestDocsTestSupport
 * <br>date           : 2024-07-24
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
 * 2024-07-24        jack8              init create
 * </pre>
 */

@Disabled
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Import({RestDocsConfiguration.class, SecurityConfig.class})
@ExtendWith(RestDocumentationExtension.class)
public class RestDocsSupportTest {

    @Autowired
    protected RestDocumentationResultHandler restDocs;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected JdbcTemplate  jdbcTemplate;
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected AuthenticationManager authenticationManager;
    @Autowired
    protected JwtProvider jwtProvider;
    @Autowired
    protected RedisRepository redisRepository;
    @Autowired
    protected MinioRepository minioRepository;

    @Value("${minio.bucketName}")
    protected String bucketName;

    @RegisterExtension
    protected static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP.dynamicPort())
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("dummy", "dummy"))
            .withPerMethodLifecycle(false);

//    @DynamicPropertySource
//    static void registerGreenMailProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.mail.port", () -> greenMail.getSmtp().getPort());
//    }

    protected final String testUsername = "test-user";
    protected final String testPassword = "!1Password1!";
    protected final String testNickname = "test-nick";

    protected static Long stackId = 1L;

    protected final UpdateProfileDto initProfileInfo = UpdateProfileDto.builder()
            .name("John")
            .gender("man")
            .age(20)
            .birth(LocalDate.of(1999,1,1))
            .description("hello")
            .build();



    @BeforeEach
    void setUp(final WebApplicationContext context, final RestDocumentationContextProvider provider) {

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(MockMvcResultHandlers.print())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @AfterEach
    void afterEach() {
        deleteUser();
        deleteProfile();
    }

    protected Attributes.Attribute getFormat (final String value) {
        return key("format").value(value);
    }

    protected JwtToken createInitUser (String username, String password, String nickname, List<String> roles) {
        String insertMemberQuery = """
                INSERT INTO member (id, username, password, nickname, university_id, is_account_non_expired, is_account_non_locked, is_credential_non_expired, is_enable)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(insertMemberQuery, stackId, username, passwordEncoder.encode(password), nickname, null, true, true, true, true);


        String insertRoleQuery = "INSERT INTO member_roles (member_id, roles) VALUES (?, ?)";
        for (String role : roles) {
            jdbcTemplate.update(insertRoleQuery, stackId, role);
        }

        stackId++;

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        return jwtProvider.generateToken(authentication);
    }

    protected JwtToken createInitUser(String username, String password, String nickname) {
        return createInitUser(username, password, nickname, List.of("GUEST"));
    }

    protected JwtToken createInitUser(String username, String nickname) {
        return createInitUser(username,testPassword, nickname);
    }

    protected JwtToken createInitUser(String username) {
        return createInitUser(username, testNickname);
    }

    protected JwtToken createInitUser() {
        return createInitUser(testUsername);
    }

    protected void deleteRedisUser(String username) {
        redisRepository.delete("refreshToken:" + username);
    }

    protected void deleteUser(String username) {
        String getIdQuery = """
                SELECT id
                FROM member
                WHERE username = ?
                """;
        Long id = Long.parseLong(Optional.ofNullable(jdbcTemplate.queryForObject(getIdQuery, String.class, username)).orElseThrow(RuntimeException::new));

        String deleteRoleQuery = """
                DELETE FROM member_roles
                WHERE member_id = ?
                """;
        jdbcTemplate.update(deleteRoleQuery, id);
        String deleteMemberQuery = """
                DELETE FROM member
                WHERE username = ?
                """;
        jdbcTemplate.update(deleteMemberQuery, username);
    }

    protected void deleteUser() {
        String deleteRoleQuery = """
                DELETE FROM member_roles
                """;
        jdbcTemplate.update(deleteRoleQuery);
        String deleteMemberQuery = """
                DELETE FROM member
                """;
        jdbcTemplate.update(deleteMemberQuery);
    }

    protected Long createInitProfile(String username) {
        String query = """
                INSERT INTO profile (id, name, gender, email, age, description, birth, is_basic_image, create_at, update_at)
                SELECT m.id, ?, ?, ?, ?, ? ,?, 1, ?, ?
                FROM member m
                WHERE m.username = ?
                """;

        jdbcTemplate.update(query,
                initProfileInfo.getName(),
                initProfileInfo.getGender(),
                "email",
                initProfileInfo.getAge(),
                initProfileInfo.getDescription(),
                initProfileInfo.getBirth(),
                Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now()),
                username

        );

        String getIdQuery = """
             SELECT id
             FROM member
             WHERE username = ?
             """;
        return jdbcTemplate.queryForObject(getIdQuery, Long.class, testUsername);
    }

    protected Long createInitProfile() {
        return createInitProfile(testUsername);
    }

    protected void deleteProfile(Long id) {
        String query = """
                DELETE FROM profile
                WHERE id = ?
                """;
        jdbcTemplate.update(query, id);
    }

    protected void deleteProfile() {
        String query = """
                DELETE FROM profile
                """;
        jdbcTemplate.update(query);
    }

    protected String getAuthHeader(JwtToken jwtToken) {
        return jwtToken.getGrantType() + " " + jwtToken.getAccessToken();
    }


}
