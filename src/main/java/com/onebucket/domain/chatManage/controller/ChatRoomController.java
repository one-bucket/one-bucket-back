package com.onebucket.domain.chatManage.controller;


import com.onebucket.domain.chatManage.domain.ChatRoom;
import com.onebucket.domain.chatManage.dto.CreateChatRoomDto;
import com.onebucket.domain.chatManage.service.ChatRoomService;
import com.onebucket.global.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


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
@Controller
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final SecurityUtils securityUtils;

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
    public ChatRoom createRoom(@Valid @RequestBody CreateChatRoomDto createChatRoomDto) {
        return chatRoomService.createChatRoom(createChatRoomDto);
    }

    // 채팅방 입장 화면
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(@PathVariable String roomId, Model model) {
        model.addAttribute("roomId", roomId);
        String username = securityUtils.getCurrentUsername();
        chatRoomService.addChatRoomMember(roomId,username);
        return "roomdetail";
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatRoomService.getChatRoom(roomId);
    }
}
