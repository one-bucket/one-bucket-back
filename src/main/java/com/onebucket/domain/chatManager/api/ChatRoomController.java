package com.onebucket.domain.chatManager.api;

import com.onebucket.domain.chatManager.dto.ChatRoomDto;
import com.onebucket.domain.chatManager.dto.ChatRoomInfoDto;
import com.onebucket.domain.chatManager.entity.ChatRoomMemberId;
import com.onebucket.domain.chatManager.entity.TradeType;
import com.onebucket.domain.chatManager.mongo.ChatMessage;
import com.onebucket.domain.chatManager.service.*;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.tradeManage.dto.BaseTradeDto;
import com.onebucket.domain.tradeManage.dto.GroupTradeDto;
import com.onebucket.domain.tradeManage.dto.UsedTradeDto;
import com.onebucket.domain.tradeManage.service.GroupTradeService;
import com.onebucket.domain.tradeManage.service.UsedTradeService;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseDto;
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
import java.util.Objects;

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

    private final UsedTradeService usedTradeService;
    private final GroupTradeService groupTradeService;


    @GetMapping("/info/{roomId}")
    public ResponseEntity<ChatRoomInfoDto<? extends BaseTradeDto.Info>> getRoomInfo(@PathVariable String roomId) {

        ChatRoomDto.Info chatRoom = chatRoomService.getRoomDetails(roomId);
        BaseTradeDto.Info trade;

        TradeType tradeType = chatRoom.getTradeType();
        Long tradeId = chatRoom.getTradeId();

        ChatRoomInfoDto<?> chatRoomInfoDto = null;

        if (Objects.requireNonNull(tradeType) == TradeType.USED) {
            trade = usedTradeService.getInfo(tradeId);

            chatRoomInfoDto = ChatRoomInfoDto.builder()
                    .trade((UsedTradeDto.Info) trade)
                    .chatRoom(chatRoom)
                    .build();
        } else if (tradeType == TradeType.GROUP) {
            trade = groupTradeService.getInfo(tradeId);

            chatRoomInfoDto = ChatRoomInfoDto.builder()
                    .trade((GroupTradeDto.Info) trade)
                    .chatRoom(chatRoom)
                    .build();
        }

        return ResponseEntity.ok(chatRoomInfoDto);
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

    @DeleteMapping("/push/un-register/{chatRoomId}")
    public ResponseEntity<SuccessResponseDto> unRegisterPushMessage(@PathVariable String chatRoomId) {
        Long userId = securityUtils.getUserId();
        chatRoomService.unRegisterChatToken(chatRoomId, userId);
        return ResponseEntity.ok(new SuccessResponseDto("unregister to send push message"));
    }

    @PostMapping("/push/re-register/{chatRoomId}")
    public ResponseEntity<SuccessResponseDto> reRegisterPushMessage(@PathVariable String chatRoomId) {
        Long userId = securityUtils.getUserId();
        chatRoomService.reRegisterChatToken(chatRoomId, userId);
        return ResponseEntity.ok(new SuccessResponseDto("success re-register push message"));

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
