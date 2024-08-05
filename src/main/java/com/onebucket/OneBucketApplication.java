package com.onebucket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class OneBucketApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneBucketApplication.class, args);
    }

}
