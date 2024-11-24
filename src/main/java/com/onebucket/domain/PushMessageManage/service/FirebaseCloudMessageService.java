package com.onebucket.domain.PushMessageManage.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.MulticastMessage;
import com.onebucket.domain.PushMessageManage.dto.FcmMessage;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.bson.json.JsonParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/" +
                                   "one-bucket/messages:send";
    private final ObjectMapper objectMapper;

    public void sendMessageToToken(String targetToken, String title, String body) {
        try {
            String message = makeMessageToToken(targetToken, title, body);

            Request request = makeRequest(message);

            OkHttpClient client = new OkHttpClient();

            client.newCall(request).execute();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }


    }

    public void sendMessageToToken(List<String> targetTokens, String title, String body) {
        MulticastMessage message = MulticastMessage.builder()
                .putData("title", title)
                .putData("body", body)
                .addAllTokens(targetTokens)
                .build();

        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
            System.out.println("success message count" + response.getSuccessCount());
            System.out.println("failure message count" + response.getFailureCount());
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void sendMessageToTopic(String topic, String title, String body) throws IOException {
        String message = makeMessageToTopic(topic, title, body);

        Request request = makeRequest(message);

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());
    }

    private Request makeRequest(String message) throws IOException {
        RequestBody requestBody = RequestBody.create(
                message,
                MediaType.get("application/json; charset=utf-8")
        );

        return new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();
    }

    private String makeMessageToToken(String targetTokens, String title, String body)
            throws JsonParseException, JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message((FcmMessage.Message.builder()
                        .token(targetTokens)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build())
                        .build()))
                .validateOnly(false)
                .build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String makeMessageToTopic(String topic, String title, String body)
            throws JsonParseException, JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .topic(topic)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build())
                        .build())
                .validateOnly(false)
                .build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/firebase_service_key.json";
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
