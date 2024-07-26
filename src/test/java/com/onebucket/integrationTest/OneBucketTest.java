package com.onebucket.integrationTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Objects;

/**
 * <br>package name   : com.onebucket.integrationTest
 * <br>file name      : OneBucketTest
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
public class OneBucketTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Test
    void contextLoads() throws SQLException {


        DatabaseMetaData metaData = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection().getMetaData();
        String databaseProductName = metaData.getDatabaseProductName();
        Assertions.assertEquals("H2", databaseProductName, "Database is not H2");


    }
}
