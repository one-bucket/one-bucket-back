package com.onebucket.integrationTest.ApiTest;

import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import com.onebucket.testComponent.testSupport.RestDocsSupportTest;
import com.onebucket.testComponent.testSupport.UserRestDocsSupportTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

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
public class PostTest extends UserRestDocsSupportTest {

    // POST TEST

    @Test
    @DisplayName("GET /post/list/{boardId}")
    @Sql(scripts = "/sql/InitDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getPostByBoard() throws Exception {
        JwtToken jwtToken = createInitUser();
    }

}
