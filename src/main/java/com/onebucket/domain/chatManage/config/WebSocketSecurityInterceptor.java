package com.onebucket.domain.chatManage.config;

import com.onebucket.global.auth.jwtAuth.component.JwtValidator;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * <br>package name   : com.onebucket.domain.chatManage.config
 * <br>file name      : ChatPreHandler
 * <br>date           : 2024-07-24
 * <pre>
 * <span style="color: white;">[description]</span>
 * Websocket && Stomp 통신에서 stomp header에 있는 JWT가 유효한지 검사한다.
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
@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebSocketSecurityInterceptor implements ChannelInterceptor {

    private final JwtValidator jwtValidator;

    @Override
    public Message<?> preSend(@NotNull Message<?> message, @NotNull MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
        //StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message); 이렇게 작성하면 회원 role 체크가 안됨

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtValidator.isTokenValid(token)) {
                Authentication authentication = jwtValidator.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                accessor.setUser(authentication);
            } else {
                throw new JwtException("Invalid JWT token");
            }
        } else {
            log.info("Authorization header is missing or invalid");
            throw new JwtException("Authorization header is missing or invalid");
        }
        return message;
    }
}
