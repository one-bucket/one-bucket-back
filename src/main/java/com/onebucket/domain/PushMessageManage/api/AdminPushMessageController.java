package com.onebucket.domain.PushMessageManage.api;

import com.onebucket.domain.PushMessageManage.dto.PushMessageRequestDto;
import com.onebucket.domain.PushMessageManage.service.FirebaseCloudMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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


    @PostMapping("/test/push/send")
    public ResponseEntity<?> pushMessage(@RequestBody PushMessageRequestDto requestDTO) throws IOException {
        System.out.println(requestDTO.getTargetToken() + " "
                +requestDTO.getTitle() + " " + requestDTO.getBody());

        firebaseCloudMessageService.sendMessageToToken(
                requestDTO.getTargetToken(),
                requestDTO.getTitle(),
                requestDTO.getBody());
        return ResponseEntity.ok().build();
    }
}
