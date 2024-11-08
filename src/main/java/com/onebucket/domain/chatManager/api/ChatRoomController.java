package com.onebucket.domain.chatManager.api;

import com.onebucket.domain.chatManager.dto.ChatRoomDto;
import com.onebucket.domain.chatManager.entity.ChatRoomMemberId;
import com.onebucket.domain.chatManager.mongo.ChatMessage;
import com.onebucket.domain.chatManager.service.*;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.tradeManage.dto.TradeDto;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseDto;
import com.onebucket.global.utils.SuccessResponseWithIdDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.chatManager.controller
 * <br>file name      : ChatRoomController
 * <br>date           : 2024-09-16
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

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final SecurityUtils securityUtils;
    private final MemberService memberService;
    private final SSEChatListService sseChatListService;


    @PostMapping("/room")
    public ResponseEntity<SuccessResponseDto> createRoom(@RequestParam String name) {
        String username = securityUtils.getCurrentUsername();
        Long userId = memberService.usernameToId(username);
        ChatRoomDto.CreateRoom dto = ChatRoomDto.CreateRoom.builder()
                .memberId(userId)
                .name(name)
                .build();
        String id = chatRoomService.createRoom(dto);

        return ResponseEntity.ok(new SuccessResponseDto(id));

    }

    @PostMapping("/join")
    public ResponseEntity<SuccessResponseWithIdDto> joinRoom(@RequestParam String roomId) {
        String username = securityUtils.getCurrentUsername();
        Long userId = memberService.usernameToId(username);
        ChatRoomDto.ManageMember dto = ChatRoomDto.ManageMember.builder()
                .roomId(roomId)
                .memberId(userId)
                .build();

        Long count = chatRoomService.addMember(dto);

        return ResponseEntity.ok(new SuccessResponseWithIdDto("success add member", count));
    }

    @DeleteMapping("/quit")
    public ResponseEntity<SuccessResponseDto> quitRoom(@RequestParam String roomId) {
        String username = securityUtils.getCurrentUsername();
        Long userId = memberService.usernameToId(username);
        ChatRoomDto.ManageMember dto = ChatRoomDto.ManageMember.builder()
                .roomId(roomId)
                .memberId(userId)
                .build();

        chatRoomService.quitMember(dto);

        return ResponseEntity.ok(new SuccessResponseDto("success quit member"));
    }

    @GetMapping("/memberList")
    public ResponseEntity<List<ChatRoomDto.MemberInfo>> getRoomMember(@RequestParam String roomId) {
        return ResponseEntity.ok(chatRoomService.getMemberList(roomId));
    }

    @GetMapping("/logs")
    public ResponseEntity<List<ChatMessage>> getLogs(@RequestParam String roomId, @RequestParam String timestamp) {
        Instant instant = Instant.parse(timestamp);
        Date dateTimestamp = Date.from(instant);
        ChatRoomDto.InfoAfterTime infoAfterTime = ChatRoomDto.InfoAfterTime.builder()
                .roomId(roomId)
                .timestamp(dateTimestamp)
                .build();
        List<ChatMessage> messages = chatRoomService.getMessageAfterTimestamp(infoAfterTime);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/trade/{chatRoomId}")
    public ResponseEntity<TradeDto.ResponseInfo> getTradeInfo(@PathVariable("chatRoomId") String chatRoomId) {
        return ResponseEntity.ok(TradeDto.ResponseInfo.of(chatRoomService.getTradeInfoOfChatRoom(chatRoomId)));
    }

    @DeleteMapping("/bomb/{chatRoomId}")
    public ResponseEntity<SuccessResponseDto> bombRoom(@PathVariable("chatRoomId") String chatRoomId) {
        String username = securityUtils.getCurrentUsername();
        Long userId = memberService.usernameToId(username);

        ChatRoomDto.ManageMember dto = ChatRoomDto.ManageMember.builder()
                .roomId(chatRoomId)
                .memberId(userId)
                .build();
        chatRoomService.bombRoomByOwner(dto);
        return ResponseEntity.ok(new SuccessResponseDto("success bomb"));
    }

    @GetMapping("/sse/chatList")
    public SseEmitter subscribe() {
        String username = securityUtils.getCurrentUsername();
        Long userId = memberService.usernameToId(username);


        List<ChatRoomDto.ChatRoomInfo> chatRoomInfos =
                chatRoomService.getRoomIds(userId).stream().map((roomId) -> {
                    ChatRoomMemberId id = ChatRoomMemberId.builder()
                            .member(userId)
                            .chatRoom(roomId)
                            .build();
                    LocalDateTime localDateTimeDisconnectAt = chatRoomService.getDisconnectTime(id);
                    Date disconnectAt = Date.from(localDateTimeDisconnectAt.atZone(ZoneId.systemDefault()).toInstant());

                    ChatRoomDto.InfoAfterTime dto = ChatRoomDto.InfoAfterTime.builder()
                            .roomId(roomId)
                            .timestamp(disconnectAt)
                            .build();

                    return chatRoomService.getRoomInfo(dto);
                }).toList();


        SseEmitter emitter = sseChatListService.subscribe(userId);
        try {
            emitter.send(SseEmitter.event()
                    .name("initial-room-list")
                    .data(chatRoomInfos));
        } catch (IOException e) {
            emitter.complete();
        }

        return emitter;
    }
}
