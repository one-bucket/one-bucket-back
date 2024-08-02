package com.onebucket.global.auth.config.DBConfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * <br>package name   : com.onebucket.global.auth.config
 * <br>file name      : JpaConfig
 * <br>date           : 2024-08-02
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
 * 2024-08-02        SeungHoon              init create
 * </pre>
 */
@Configuration
@EnableJpaRepositories(basePackages = {
        "com.onebucket.domain.boardManage.dao",
        "com.onebucket.domain.memberManage.dao",
        "com.onebucket.domain.universityManage.dao"
})
public class JpaConfig {
}