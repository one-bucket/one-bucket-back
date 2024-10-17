package com.onebucket.domain.chatManager.api;

import com.onebucket.domain.chatManager.dto.ChatRoomDto;
import com.onebucket.domain.chatManager.service.ChatRoomService;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

}
