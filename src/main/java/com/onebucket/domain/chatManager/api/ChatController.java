package com.onebucket.domain.chatManager.api;

import com.onebucket.domain.chatManager.dto.ChatDto;

import com.onebucket.domain.chatManager.dto.ChatRoomDto;
import com.onebucket.domain.chatManager.service.ChatRoomService;
import com.onebucket.domain.chatManager.service.ChatService;
import com.onebucket.domain.chatManager.service.SSEChatListService;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.tradeManage.dto.TradeKeyDto;
import com.onebucket.domain.tradeManage.service.PendingTradeService;
import com.onebucket.global.exceptionManage.customException.chatManageException.ChatManageException;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import com.onebucket.global.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * <br>package name   : com.onebucket.domain.chatManager.controller
 * <br>file name      : ChatController
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
@RequiredArgsConstructor
@RestController
@Slf4j
public class ChatController {
    private final SimpMessageSendingOperations template;
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;
    private final SSEChatListService sseChatListService;
    private final MemberService memberService;
    private final PendingTradeService pendingTradeService;

    private final ImageUtils imageUtils;

    @MessageMapping("/message")
    public void message(@Payload ChatDto chat, SimpMessageHeaderAccessor headerAccessor) {
        switch (chat.getType()) {
            case ENTER -> enterUser(chat);
            case TALK -> sendMessage(chat);
            case LEAVE -> leaveUser(chat, headerAccessor);
            default -> throw new ChatManageException(ChatErrorCode.MESSAGING_ERROR);
        }
    }

    private void enterUser(ChatDto chat) {
        chat.setMessage(chat.getSender() + "님이 입장하였습니다.");

        chatService.saveMessage(chat);

        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);
    }

    private void sendMessage(ChatDto chat) {
        chat.setMessage(chat.getMessage());

        chatService.saveMessage(chat);
        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);
        sseChatListService.notifyRoomUpdate(chat);
    }

    private void leaveUser(ChatDto chat, SimpMessageHeaderAccessor headerAccessor) {
        chat.setMessage(chat.getSender() + "님이 퇴장하였습니다.");
        chatService.saveMessage(chat);
        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(),chat);

        Authentication authentication = (Authentication) headerAccessor.getUser();
        if(authentication != null) {
            String username = authentication.getName();
            Long userId = memberService.usernameToId(username);

            ChatRoomDto.ManageMember dto = ChatRoomDto.ManageMember.builder()
                    .roomId(chat.getRoomId())
                    .memberId(userId)
                    .build();

            //거래 정보에서도 삭제
            ChatRoomDto.GetTradeInfo tradeInfo = chatRoomService.getTradeInfo(chat.getRoomId());
            Long tradeId = tradeInfo.getId();
            TradeKeyDto.UserTrade userTrade = TradeKeyDto.UserTrade.builder()
                    .tradeId(tradeId)
                    .userId(userId)
                    .build();
            pendingTradeService.quitMember(userTrade);

            chatRoomService.quitMember(dto);
        }
    }

    private void imageMessage(ChatDto chat) {
        String message = chat.getMessage();
        String imageFormat = imageUtils.getFileExtensionFromMessage(message);
        String fileName = imageUtils.getFileNameFromMessage(message);

        ChatRoomDto.SaveImage saveImageDto = ChatRoomDto.SaveImage.builder()
                .format(imageFormat)
                .build();

    }

}
