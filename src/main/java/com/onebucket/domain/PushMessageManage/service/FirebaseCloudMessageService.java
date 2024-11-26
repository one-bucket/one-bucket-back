package com.onebucket.domain.PushMessageManage.service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.onebucket.domain.PushMessageManage.dto.PushMessageDto;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * <br>package name   : com.onebucket.domain.PushMessageManage.service
 * <br>file name      : FirebaseCloudMessageService
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
@Component
@RequiredArgsConstructor
public class FirebaseCloudMessageService {

    @Async
    public void sendMessageToToken(PushMessageDto.Token dto) {
        if(dto.getToken().isEmpty()) {
            return;
        }

        Message message = Message.builder()
                .putData("title", dto.getTitle())
                .putData("body", dto.getBody())
                .putData("type" , dto.getType().name())
                .putData("id", dto.getId())
                .setToken(dto.getToken())

                .build();
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Async
    public void sendMessageToToken(PushMessageDto.Tokens dto) {
        if(dto.getTokens().isEmpty()) {
            System.out.println("empty token");
            return;
        }
        MulticastMessage message = MulticastMessage.builder()
                .putData("title", dto.getTitle())
                .putData("body", dto.getBody())
                .putData("type" , dto.getType().name())
                .putData("id", dto.getId())
                .addAllTokens(dto.getTokens())

                .build();

        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);
            System.out.println("success count is " + response.getSuccessCount());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Async
    public void sendMessageToTopic(PushMessageDto.Topic dto) {
        if (dto.getTopic().isEmpty()) {
            return;
        }

        Message message = Message.builder()
                .putData("title", dto.getTitle())
                .putData("body", dto.getBody())
                .putData("type" , dto.getType().name())
                .putData("id", dto.getId())
                .setTopic(dto.getTopic())
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
