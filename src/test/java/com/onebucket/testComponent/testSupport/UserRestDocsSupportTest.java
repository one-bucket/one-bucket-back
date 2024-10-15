package com.onebucket.testComponent.testSupport;

import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import org.junit.jupiter.api.AfterEach;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * <br>package name   : com.onebucket.testComponent.testSupport
 * <br>file name      : UserRestDocsSupportTest
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
public class UserRestDocsSupportTest extends RestDocsSupportTest {

    protected final String testUsername = "testuser1";
    protected final String testPassword = "!1Password1!";
    protected final String testNickname = "test1";

    protected Long stackId = 1L;


    @Override
    @AfterEach
    protected void after() {
        super.after();

        stackId = 1L;
    }

    protected JwtToken createInitUser() {

        String insertMemberQuery = """
                INSERT INTO member (id, username, password, nickname, university_id, is_account_non_expired, is_account_non_locked, is_credential_non_expired, is_enable)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(insertMemberQuery,
                stackId, testUsername, passwordEncoder.encode(testPassword), testNickname, null, true, true, true, true);

        String insertRoleQuery = "INSERT INTO member_roles (member_id, roles) VALUES (?, ?)";
        jdbcTemplate.update(insertRoleQuery, stackId, "GUEST");

        String insertProfileQuery = """
                INSERT INTO profile (id, age, birth, create_at, description, gender, image_url, is_basic_image, name, update_at)
                VALUES (?, 26, '1999-01-01', '2024-01-01', '안녕 친구들', 'man', null , 1, ?, '2024-01-01');
                """;

        String name = "name" + stackId;
        jdbcTemplate.update(insertProfileQuery, stackId, name);


        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(testUsername, testPassword);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        stackId++;
        return jwtProvider.generateToken(authentication);
    }
}
