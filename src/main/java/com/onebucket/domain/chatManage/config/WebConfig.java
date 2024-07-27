//package com.onebucket.domain.chatManage.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
///**
// * <br>package name   : com.onebucket.domain.chatManage.config
// * <br>file name      : WebConfig
// * <br>date           : 2024-07-26
// * <pre>
// * <span style="color: white;">[description]</span>
// * CORS 관련 설정하기.
// * </pre>
// * <pre>
// * <span style="color: white;">usage:</span>
// * {@code
// *
// * } </pre>
// * <pre>
// * modified log :
// * =======================================================
// * DATE           AUTHOR               NOTE
// * -------------------------------------------------------
// * 2024-07-26        SeungHoon              init create
// * </pre>
// */
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOriginPatterns("*") // 모든 출처 허용
//                .allowedMethods("*")
//                .allowedHeaders("*")
//                .allowCredentials(true)
//                .maxAge(3600L); // 1시간
//    }
//}