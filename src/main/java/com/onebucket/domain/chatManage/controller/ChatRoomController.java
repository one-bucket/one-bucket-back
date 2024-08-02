package com.onebucket.domain.chatManage.controller;


import com.onebucket.domain.chatManage.domain.ChatRoom;
import com.onebucket.domain.chatManage.service.ChatRoomService;
import com.onebucket.global.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

/**
 * <br>package name   : com.onebucket.domain.chatManage.controller
 * <br>file name      : ChatRoomController
 * <br>date           : 2024-07-09
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
 * 2024-07-09        SeungHoon              init create
 * </pre>
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final SecurityUtils securityUtils;

    // 모든 채팅방 목록 반환
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoom>> getRooms() {
        List<ChatRoom> response = chatRoomService.getChatRooms();
        return ResponseEntity.ok(response);
    }

    // 채팅방 생성
    @PostMapping("/room")
    public ResponseEntity<Void> createRoom(@RequestParam String name) {
        chatRoomService.createChatRoom(name);
        return ResponseEntity.ok().build();
    }

    // 채팅방 입장
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ChatRoom> enterRoom(@PathVariable String roomId) {
        String username = securityUtils.getCurrentUsername();
        chatRoomService.addChatRoomMember(roomId,username);
        ChatRoom response = chatRoomService.getChatRoom(roomId);
        return ResponseEntity.ok(response);
    }
}
