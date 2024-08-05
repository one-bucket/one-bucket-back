package com.onebucket.domain.chatManage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

/**
 * <br>package name   : com.onebucket.global.auth.config
 * <br>file name      : WebSocketSecurityConfig
 * <br>date           : 2024-07-24
 * <pre>
 * <span style="color: white;">[description]</span>
 * Websocket 보안 관련 코드.
 * "/ws-stomp" 로 오는 메세지는 모두 허용
 * "/pub", "/sub" 으로 오는 메세지는 모두 인증이 필요하다.
 * CSRF 비활성화
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
 * 2024-07-24        SeungHoon              init create
 * </pre>
 */
@EnableWebSocketSecurity
@Configuration
public class WebSocketSecurityConfig {
    @Bean
    public AuthorizationManager<Message<?>> messageAuthorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        return messages
                .nullDestMatcher().permitAll()
                .simpDestMatchers("/ws-stomp/**").permitAll()
                .simpDestMatchers("/pub/**").authenticated()
                .simpSubscribeDestMatchers("/sub/**").authenticated()
                .anyMessage().denyAll()
                .build();
    }

    @Bean("csrfChannelInterceptor") // for disable csrf
    public ChannelInterceptor csrfChannelInterceptor() {
        return new ChannelInterceptor() {
        };
    }
}
