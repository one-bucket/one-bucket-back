package com.onebucket.testComponent.testSupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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



    protected Attributes.Attribute getFormat (final String value) {
        return key("format").value(value);
    }

    protected void flushRedis() {
        redisRepository.flushAll();;
    }

    protected String getAuthHeader(JwtToken jwtToken) {
        return jwtToken.getGrantType() + " " + jwtToken.getAccessToken();
    }

}
