package com.onebucket.domain.PushMessageManage.api;

import com.onebucket.domain.PushMessageManage.dto.PushMessageDto;
import com.onebucket.domain.PushMessageManage.dto.PushMessageRequestDto;
import com.onebucket.domain.PushMessageManage.dto.PushMessageType;
import com.onebucket.domain.PushMessageManage.dto.TopicMessageRequestDto;
import com.onebucket.domain.PushMessageManage.service.FcmDataManageService;
import com.onebucket.domain.PushMessageManage.service.FirebaseCloudMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <br>package name   : com.onebucket.domain.PushMessageManage.api
 * <br>file name      : AdminPushMessageController
 * <br>date           : 2024-11-03
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
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminPushMessageController {
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final FcmDataManageService fcmDataManageService;


    @PostMapping("/push/send")
    public ResponseEntity<?> pushMessage(@RequestBody PushMessageRequestDto dto) {

        String token = fcmDataManageService.getTokensByUserId(dto.getUserId());
        PushMessageDto.Token sendDto = PushMessageDto.Token.builder()
                .title(dto.getTitle())
                .body(dto.getBody())
                .type(PushMessageType.WARNING)
                .token(token)
                .build();

        firebaseCloudMessageService.sendMessageToToken(sendDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/push/all")
    public ResponseEntity<?> topicMessage(@RequestBody TopicMessageRequestDto dto) {
        PushMessageDto.Topic sendDto = PushMessageDto.Topic.builder()
                .title(dto.getTitle())
                .body(dto.getBody())
                .type(PushMessageType.ALL)
                .topic("ALL_USER")
                .build();
        firebaseCloudMessageService.sendMessageToTopic(sendDto);

        return ResponseEntity.ok().build();
    }
}
