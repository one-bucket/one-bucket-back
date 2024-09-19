package com.onebucket.domain.chatManage.config;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.simp.stomp.*;

/**
 * <br>package name   : com.onebucket.domain.chatManage.config
 * <br>file name      : StompSessionHandler
 * <br>date           : 2024-09-18
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
 * 2024-09-18        SeungHoon              init create
 * </pre>
 */
@Slf4j
public class MyStompSessionHandler extends StompSessionHandlerAdapter {

    @Override
    public void afterConnected(@NotNull StompSession session, @NotNull StompHeaders connectedHeaders) {
        System.out.println("Connected to the server");
    }

    @Override
    public void handleException(@NotNull StompSession session, StompCommand command, @NotNull StompHeaders headers, @NotNull byte[] payload, Throwable exception) {
        log.error("Error during STOMP command {}: {}", command, exception.getMessage());
        // 재시도 로직 추가 가능
        if (command == StompCommand.SEND) {
            log.warn("Error while sending message: {}", new String(payload));
            // 추가 처리 로직
        }
    }

    @Override
    public void handleTransportError(@NotNull StompSession session, Throwable exception) {
        log.error("Transport error: {}", exception.getMessage());
        if (exception instanceof ConnectionLostException) {
            log.info("Connection lost, attempting to reconnect...");
            // 재연결 시도 로직
        }
    }

}

