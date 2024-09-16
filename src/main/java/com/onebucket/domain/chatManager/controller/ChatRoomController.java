package com.onebucket.domain.chatManager.controller;

import com.onebucket.domain.chatManager.dto.ChatRoom;
import com.onebucket.domain.chatManager.repository.ChatRepository;
import com.onebucket.global.utils.SuccessResponseDto;
import com.onebucket.global.utils.SuccessResponseWithIdDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
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
@RequestMapping("/chat/room")
public class ChatRoomController {
    private final ChatRepository chatRepository;

    @GetMapping("/")
    public ResponseEntity<List<ChatRoom>> goChatRoom() {
        List<ChatRoom> chatRooms = chatRepository.findAllRoom();
        return ResponseEntity.ok(chatRooms);
    }

    @PostMapping("/room")
    public ResponseEntity<ChatRoom> createRoom(@RequestParam String name) {
        ChatRoom room = chatRepository.createChatRoom(name);
        return ResponseEntity.ok(room);

    }

    @GetMapping("/userList")
    public ArrayList<String> userList(String roomId) {
        return chatRepository.getUserList(roomId);
    }
}
