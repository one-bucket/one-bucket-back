//package com.onebucket.connectionTest;
//
//import com.onebucket.domain.chatManage.config.CustomStompSessionHandler;
//import com.onebucket.domain.memberManage.dao.MemberRepository;
//import com.onebucket.domain.memberManage.dto.CreateMemberRequestDto;
//import com.onebucket.domain.memberManage.service.MemberServiceImpl;
//import com.onebucket.domain.memberManage.service.SignInServiceImpl;
//import com.onebucket.global.auth.jwtAuth.component.JwtValidator;
//import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
//import lombok.extern.slf4j.Slf4j;
//import org.jetbrains.annotations.NotNull;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.messaging.converter.StringMessageConverter;
//import org.springframework.messaging.simp.stomp.StompFrameHandler;
//import org.springframework.messaging.simp.stomp.StompHeaders;
//import org.springframework.messaging.simp.stomp.StompSession;
//import org.springframework.messaging.simp.stomp.StompSessionHandler;
//import org.springframework.web.socket.WebSocketHttpHeaders;
//import org.springframework.web.socket.client.WebSocketClient;
//import org.springframework.web.socket.client.standard.StandardWebSocketClient;
//import org.springframework.web.socket.messaging.WebSocketStompClient;
//
//import java.lang.reflect.Type;
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
///**
// * <br>package name   : com.onebucket.connectionTest
// * <br>file name      : StompConnectTest
// * <br>date           : 2024-09-18
// * <pre>
// * <span style="color: white;">[description]</span>
// *
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
// * 2024-09-18        SeungHoon              init create
// * </pre>
// */
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@Slf4j
//public class StompConnectTest {
//    @Autowired
//    private MemberServiceImpl memberServiceImpl;
//    @Autowired
//    private SignInServiceImpl signInServiceImpl;
//    @Autowired
//    private MemberRepository memberRepository;
//    @Autowired
//    private JwtValidator jwtValidator;
//
//    @Test
//    @DisplayName("stomp 연결 테스트 성공")
//    public void testStompConnection_success() throws Exception {
//
//        final String roomId = "room1";
//        final String username = "user1";
//        final String password = "password";
//        final String nickname = "nicknick";
//        // stompClient를 만든다.
//        WebSocketClient webSocketClient = new StandardWebSocketClient();
//        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
//        stompClient.setMessageConverter(new StringMessageConverter());
//        if(!memberRepository.existsByUsername(username)){
//            CreateMemberRequestDto dto = CreateMemberRequestDto.builder()
//                    .username(username)
//                    .password(password)
//                    .nickname(nickname)
//                    .build();
//            memberServiceImpl.createMember(dto);
//        }
//        JwtToken token = signInServiceImpl.signInByUsernameAndPassword(username, password);
//        // 5. WebSocketHttpHeaders를 사용하여 헤더에 JWT 추가
//        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
//        headers.add("Authorization", "Bearer " + token.getAccessToken());  // JWT 토큰 추가
//
//        log.info("access token : {}",token.getAccessToken());
//        log.info("refresh token : {}",token.getRefreshToken());
//        log.info("access token is valid? : {}",jwtValidator.isTokenValid(token.getAccessToken()));
//        log.info("refresh token is valid? : {}",jwtValidator.isTokenValid(token.getRefreshToken()));
//        String url = "ws://localhost:8080/ws-stomp";
//        StompSessionHandler sessionHandler = new CustomStompSessionHandler();
//        StompSession stompSession = stompClient.connect(url, headers, sessionHandler).get();
//
//        // stompClient가 메세지를 발행하고 구독해본다.
//        stompSession.send("/pub/chat/message", "Hello, STOMP!");
//        stompSession.subscribe("/sub/chat/room"+roomId, new StompFrameHandler() {
//            @Override
//            public Type getPayloadType(@NotNull StompHeaders headers) {
//                return String.class;
//            }
//
//            @Override
//            public void handleFrame(@NotNull StompHeaders headers, Object payload) {
//                assertEquals("Reply from server", payload);
//            }
//        });
//        stompSession.disconnect();
//    }
//}
