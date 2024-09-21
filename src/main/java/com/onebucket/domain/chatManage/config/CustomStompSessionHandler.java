//package com.onebucket.domain.chatManage.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.messaging.simp.stomp.*;
//
///**
// * <br>package name   : com.onebucket.domain.chatManage.config
// * <br>file name      : StompSessionHandler
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
//@Slf4j
//public class CustomStompSessionHandler extends StompSessionHandlerAdapter {
//
//    @Override
//    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
//        System.out.println("Connected to WebSocket server.");
//        // 필요한 추가 로직
//    }
//
//    @Override
//    public void handleFrame(StompHeaders headers, Object payload) {
//        System.out.println("Received frame: " + payload);
//    }
//
//    @Override
//    public void handleTransportError(StompSession session, Throwable exception) {
//        System.err.println("Transport error: " + exception.getMessage());
//    }
//}
//
