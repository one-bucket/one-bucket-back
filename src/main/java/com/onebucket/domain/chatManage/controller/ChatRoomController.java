package com.onebucket.domain.chatManage.controller;


import com.onebucket.domain.chatManage.domain.ChatRoom;
import com.onebucket.domain.chatManage.dto.CreateChatRoomDto;
import com.onebucket.domain.chatManage.service.ChatRoomService;
import com.onebucket.global.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.List;

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

    // 모든 채팅방 목록 조회
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoom>> getRooms() {
        List<ChatRoom> response = chatRoomService.getChatRooms();
        return ResponseEntity.ok(response);
    }

    // 채팅방 생성
    @PostMapping("/room")
    public ResponseEntity<Void> createRoom(@Valid @RequestBody CreateChatRoomDto dto) {
        String roomId = chatRoomService.createChatRoom(dto);
        URI location = URI.create(String.format("/chat/room/%s", roomId));
        return ResponseEntity.created(location).build();
    }

    // 특정 채팅방에 입장하기
    @PostMapping("/room/{roomId}/enter")
    public ResponseEntity<ChatRoom> enterRoom(@PathVariable String roomId) {
        String username = securityUtils.getCurrentUsername();
        ChatRoom response = chatRoomService.addChatMembers(roomId, username);
        return ResponseEntity.ok(response);
    }

    // 특정 채팅방에서 나가기
    @DeleteMapping("/room/{roomId}/leave")
    public ResponseEntity<ChatRoom> leaveRoom(@PathVariable String roomId) {
        String username = securityUtils.getCurrentUsername();
        ChatRoom response = chatRoomService.removeChatMember(roomId, username);
        return ResponseEntity.ok(response);
    }

    // 특정 유저가 입장해 있는 채팅방 목록 조회
    @GetMapping("/room/user/{nickname}")
    public ResponseEntity<List<ChatRoom>> getRoomsForMember(@PathVariable String nickname) {
        List<ChatRoom> response = chatRoomService.findByMembersNickname(nickname);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable String roomId) {
        String username = securityUtils.getCurrentUsername();
        chatRoomService.deleteChatRoom(roomId,username);
        return ResponseEntity.ok().build();
    }
}
