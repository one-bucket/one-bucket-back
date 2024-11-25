package com.onebucket.domain.PushMessageManage.service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.List;

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
    public void sendMessageToToken(String targetToken, String title, String body) {
        if(targetToken.isEmpty()) {
            return;
        }

        Message message = Message.builder()
                .putData("title", title)
                .putData("body", body)
                .setToken(targetToken)
                .build();
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Async
    public void sendMessageToToken(List<String> targetTokens, String title, String body) {
        if(targetTokens.isEmpty()) {
            System.out.println("empty token");
            return;
        }
        MulticastMessage message = MulticastMessage.builder()
                .putData("title", title)
                .putData("body", body)
                .addAllTokens(targetTokens)
                .build();

        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);
            System.out.println("success count is " + response.getSuccessCount());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Async
    public void sendMessageToTopic(String topic, String title, String body) {
        if (topic.isEmpty()) {
            return;
        }

        Message message = Message.builder()
                .putData("title", title)
                .putData("body", body)
                .setTopic(topic)
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
