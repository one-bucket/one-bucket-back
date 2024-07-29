package com.onebucket.domain.chatManage.controller;

import com.onebucket.domain.chatManage.dao.ChatRoomRepository;
import com.onebucket.domain.chatManage.domain.ChatRoom;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.memberManage.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;
import java.util.Set;

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
@Controller
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;

    private final UserService userService;

    // 채팅방 리스트 화면
    @GetMapping("/room")
    public String rooms(Model model) {
        return "room";
    }

    // 모든 채팅방 목록 반환
    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> room() {
        return chatRoomService.getChatRooms();
    }

    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String name) {
        return chatRoomService.createChatRoom(name);
    }

    // 채팅방 입장 화면
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(@PathVariable String roomId, Model model) {
        model.addAttribute("roomId", roomId);
        return "roomdetail";
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public Optional<ChatRoom> roomInfo(@PathVariable String roomId) {
        return chatRoomRepository.findByRoomId(roomId);
    }
}
