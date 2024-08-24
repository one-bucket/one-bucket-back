package com.onebucket.domain.chatManage.controller;

import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.domain.chatManage.service.ChatMessageService;
import com.onebucket.global.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.chatManage.controller
 * <br>file name      : ChatMessageController
 * <br>date           : 2024-07-09
 * <pre>
 * <span style="color: white;">[description]</span>
 * "@MessageMapping("/chat/message")" :  Spring에서 WebSocket 메시지 매핑을 위해 사용.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *  public void chatMessage(ChatMessage chatMessage)
 *  public List<ChatMessage> getChatMessages(@PathVariable("roomId") String roomId)
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
@Slf4j
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SecurityUtils securityUtils;

    /**
     * 특정 채팅방에 속한 채팅을 모두 반환한다.
     * @param roomId 채팅방의 id
     * @return 채팅 메세지 List
     */
    @GetMapping("/messages/{roomId}")
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable String roomId) {
        List<ChatMessage> response = chatMessageService.getChatMessages(roomId);
        return ResponseEntity.ok(response);
    }

    /**
     * 채팅 메세지에 있는 이미지를 minio에 저장한다.
     * @param file 저장할 이미지
     * @return file 접근 경로
     */
    @PostMapping("/messages/files")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String username = securityUtils.getCurrentUsername();
        String response = chatMessageService.uploadChatImage(file,username);
        return ResponseEntity.ok(response);
    }
}
