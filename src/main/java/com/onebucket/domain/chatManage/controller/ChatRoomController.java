package com.onebucket.domain.chatManage.controller;


import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.domain.chatManage.domain.ChatRoom;
import com.onebucket.domain.chatManage.dto.chatmessage.ChatMessageDto;
import com.onebucket.domain.chatManage.dto.chatroom.CreateChatRoomDto;
import com.onebucket.domain.chatManage.dto.chatroom.RequestCreateChatRoomDto;
import com.onebucket.domain.chatManage.dto.chatroom.ResponseChatRoomListDto;
import com.onebucket.domain.chatManage.service.ChatRoomService;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
    public ResponseEntity<List<ResponseChatRoomListDto>> getRooms() {
        List<ResponseChatRoomListDto> response = chatRoomService.getChatRooms();
        return ResponseEntity.ok(response);
    }

    // 채팅방 생성
    @PostMapping("/room")
    public ResponseEntity<Void> createRoom(@Valid @RequestBody RequestCreateChatRoomDto dto) {
        CreateChatRoomDto createChatRoomDto = CreateChatRoomDto.of(dto);
        String roomId = chatRoomService.createChatRoom(createChatRoomDto);
        URI location = URI.create(String.format("/chat/room/%s", roomId));
        return ResponseEntity.created(location).build();
    }

    // 특정 유저가 입장해 있는 채팅방 목록 조회
    @GetMapping("/room/user/{nickname}")
    public ResponseEntity<List<ResponseChatRoomListDto>> getRoomsForMember(@PathVariable String nickname) {
        List<ResponseChatRoomListDto> response = chatRoomService.findByMembersNickname(nickname);
        return ResponseEntity.ok(response);
    }

    // 채팅방 삭제하기
    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<SuccessResponseDto> deleteRoom(@PathVariable String roomId) {
        String username = securityUtils.getCurrentUsername();
        chatRoomService.deleteChatRoom(roomId,username);
        return ResponseEntity.ok(new SuccessResponseDto("Delete Success!"));
    }

    // ----------------------------------- 채팅 ------------------------------------------------ //
    @GetMapping("/messages/{roomId}")
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable String roomId) {
        List<ChatMessageDto> messagesList = chatRoomService.getChatMessages(roomId);
        List<ChatMessage> response = messagesList.stream()
                .map(requestDto -> new ChatMessage(
                        requestDto.type(),
                        requestDto.message(),
                        requestDto.sender(),
                        requestDto.roomId(),
                        requestDto.imgUrl()))
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/messages/files")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String username = securityUtils.getCurrentUsername();
        String response = chatRoomService.uploadChatImage(file,username);
        return ResponseEntity.ok(response);
    }
}
