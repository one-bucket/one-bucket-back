package com.onebucket.global.webSocket;

import com.onebucket.domain.chatManager.dto.ChatRoomDto;
import com.onebucket.domain.chatManager.service.ChatRoomService;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.global.auth.jwtAuth.component.JwtParser;
import com.onebucket.global.exceptionManage.customException.chatManageException.Exceptions.ChatRoomException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <br>package name   : com.onebucket.global.webSocket
 * <br>file name      : ChatSubscriptionInterceptor
 * <br>date           : 2024-10-17
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 */
@Component
@RequiredArgsConstructor
public class ChatInterceptor implements ChannelInterceptor {
    private final ChatRoomService chatRoomService;
    private final JwtParser jwtParser;
    private final MemberService memberService;

    private final Map<String, String> sessionUserMap = new ConcurrentHashMap<>();
    private final Map<String, String> sessionRoomMap = new ConcurrentHashMap<>();

    @Override
    public Message<?> preSend(@NotNull Message<?> message, @NotNull MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        try {

            if(StompCommand.CONNECT.equals(accessor.getCommand())) {
                String jwtToken = accessor.getFirstNativeHeader("Authorization");
                if(jwtToken != null && jwtToken.startsWith("Bearer ")) {
                    jwtToken = jwtToken.substring(7);
                } else {
                    throw new AuthenticationException(AuthenticationErrorCode.NON_VALID_TOKEN);
                }

                if(!jwtParser.isTokenValid(jwtToken)) {
                    throw new AuthenticationException(AuthenticationErrorCode.NON_VALID_TOKEN);
                }

                String username = jwtParser.getAuthentication(jwtToken).getName();
                String sessionId = accessor.getSessionId();

                sessionUserMap.put(sessionId, username);
            }

            if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {

                // 구독하려는 채팅방 ID 추출
                String destination = accessor.getDestination();
                assert destination != null;
                String roomId = extractRoomIdFromDestination(destination);

                // 세션 ID를 통해 사용자 ID 가져오기
                String sessionId = accessor.getSessionId();
                String username = getUserIdBySessionId(sessionId);  // CONNECT 시 저장된 사용자 정보 활용

                if (username == null) {
                    throw new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER, "CON before SUB");
                }
                sessionRoomMap.put(sessionId, roomId);

                Long userId = memberService.usernameToId(username);
                ChatRoomDto.ManageMember findMember = ChatRoomDto.ManageMember.builder()
                        .memberId(userId)
                        .roomId(roomId)
                        .build();
                // 사용자가 해당 채팅방의 멤버인지 확인
                if (!chatRoomService.isMemberOfChatRoom(findMember)) {
                    throw new ChatRoomException(ChatErrorCode.USER_NOT_IN_ROOM);
                }

            }

        } catch (Exception e) {
            System.out.println("Fail to pre - processing when sending message");
            return null;
        }

        return message;
    }

    @Override
    public void postSend(@NotNull Message<?> message, @NotNull MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        try {
            // DISCONNECT 시 세션 정보 삭제
            if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                String sessionId = accessor.getSessionId();

                String username = getUserIdBySessionId(sessionId);
                if(username == null) {
                    return;
                }
                Long userId = memberService.usernameToId(username);
                LocalDateTime disconnectedAt = LocalDateTime.now();
                String roomId = sessionRoomMap.get(sessionId);
                if(roomId == null) {
                    return;
                }

                ChatRoomDto.SetDisconnectTime dto = ChatRoomDto.SetDisconnectTime.builder()
                        .roomId(roomId)
                        .disconnectAt(disconnectedAt)
                        .userId(userId)
                        .build();
                chatRoomService.setDisconnectTime(dto);
                sessionUserMap.remove(sessionId);
                sessionRoomMap.remove(sessionId);

            }
        } catch (Exception e) {
            System.out.println("Fail to post - processing when terminate websocket session");
            return;
        }


    }

    private String extractRoomIdFromDestination(String destination) {
        // 예: "/sub/chat/room/{roomId}" 형식에서 {roomId}만 추출
        if (destination != null && destination.contains("/chat/room/")) {
            return destination.substring(destination.lastIndexOf("/") + 1);
        }
        throw new IllegalArgumentException("Invalid destination format: " + destination);
    }
    public String getUserIdBySessionId(String sessionId) {
        return sessionUserMap.get(sessionId);
    }


}
