package com.onebucket.connectionTest;

import com.onebucket.domain.chatManage.config.MyStompSessionHandler;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.memberManage.dto.CreateMemberRequestDto;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.memberManage.service.MemberServiceImpl;
import com.onebucket.domain.memberManage.service.SignInServiceImpl;
import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.MemberManageException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <br>package name   : com.onebucket.connectionTest
 * <br>file name      : StompConnectTest
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Slf4j
public class StompConnectTest {
    @Test
    @DisplayName("stomp 연결 테스트 성공")
    public void testStompConnection_success() throws Exception {

        String roomId = "room1";
        // stompClient를 만든다.
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new StringMessageConverter());

        StompHeaders headers = new StompHeaders();
        String url = "ws://localhost:8080/ws-stomp";
        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        StompSession stompSession = stompClient.connectAsync(url, sessionHandler, headers).get();

        // stompClient가 메세지를 발행하고 구독해본다.
        stompSession.send("/pub/chat/message", "Hello, STOMP!");
        stompSession.subscribe("/sub/chat/room"+roomId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(@NotNull StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(@NotNull StompHeaders headers, Object payload) {
                assertEquals("Reply from server", payload);
            }
        });
        stompSession.disconnect();

        Thread.sleep(1000);  // Wait for the server's response
    }
}
