package com.onebucket.global.minio;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <br>package name   : com.onebucket.global.minio
 * <br>file name      : MinioConfig
 * <br>date           : 2024-07-02
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 * http://jack8226.ddns.net:3005/으로 현재 서버가 배포되어있어, 나는 이렇게 사용할 예정이다.
 * } </pre>
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-07-02        jack8              init create
 * </pre>
 */
@Configuration
public class MinioConfig {

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint("http://jack8226.ddns.net:3100/")
                .credentials("jack8226", "m7128226")
                .build();
    }
}
