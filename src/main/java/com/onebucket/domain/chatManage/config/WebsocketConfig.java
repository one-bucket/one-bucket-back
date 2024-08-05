package com.onebucket.domain.chatManage.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 * <br>package name   : com.onebucket.global.auth.config
 * <br>file name      : WebsocketConfig
 * <br>date           : 2024-07-08
 * <pre>
 * <span style="color: white;">[description]</span>
 * Websocket 관련 config 파일이다.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 * public void configureMessageBroker(MessageBrokerRegistry config) : 스프링 내장 메세지 브로커를 사용하며, 각각 구독은 "/sub", 발행은 "/pub"을 사용한다.
 * public void registerStompEndpoints(StompEndpointRegistry registry) : 채팅은 stomp 을 사용하며, "/ws-stomp" 엔드포인트를 사용한다. 일단 모든 곳에서 오는 요청을 허용한다.
 * public void configureClientInboundChannel(ChannelRegistration registration) : 들어오는 메세지에 대한 추가적인 보안 설정
 * } </pre>
 * <pre>
 * modified log :
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-07-08        SeungHoon              init create
 * </pre>
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketSecurityInterceptor webSocketSecurityInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub");
        config.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketSecurityInterceptor);
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(50 * 1024 * 1024); // 메세지 크기 제한 오류 방지(이 코드가 없으면 byte code를 보낼때 소켓 연결이 끊길 수 있음)
    }
}
