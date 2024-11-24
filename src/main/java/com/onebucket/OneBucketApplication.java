package com.onebucket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableReactiveMongoRepositories(basePackages = "com.onebucket.domain.chatManager.mongo")
public class OneBucketApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneBucketApplication.class, args);
    }

}
