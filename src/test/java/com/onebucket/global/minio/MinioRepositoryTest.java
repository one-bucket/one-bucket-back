package com.onebucket.global.minio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onebucket.domain.chatManage.domain.ChatMessage;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * <br>package name   : com.onebucket.global.minio
 * <br>file name      : MinioRepositoryTest
 * <br>date           : 2024-07-23
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
 * 2024-07-23        SeungHoon              init create
 * </pre>
 */
@DataJpaTest
class MinioRepositoryTest {

    @Mock
    private MinioClient minioClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private MinioSaveInfoDto dto;

    @InjectMocks
    private MinioRepository minioRepository;

    ChatMessage chatMessage1;
    ChatMessage chatMessage2;
    String jsonMessage1;
    String jsonMessage2;
    @BeforeEach
    void setUp() {
        dto = new MinioSaveInfoDto("test-bucket", "testFile", "json");

        chatMessage1 = new ChatMessage();
        chatMessage2 = new ChatMessage();

        jsonMessage1 = "{\"message\":\"test message1\"}";
        jsonMessage2 = "{\"message\":\"test message2\"}";
    }

    @Test
    void uploadChatDto() throws Exception {
        when(objectMapper.writeValueAsString(any(Object.class))).thenReturn(jsonMessage1);

        minioRepository.uploadChatDto(chatMessage1,dto);

        verify(minioClient, times(1)).putObject(any(PutObjectArgs.class));
    }

    @Test
    void getChatMessages() throws Exception {
        String bucketName = "test-bucket";
        String roomId = "testRoom";
        List<ChatMessage> expectedMessages = new ArrayList<>();
        expectedMessages.add(chatMessage1);
        expectedMessages.add(chatMessage2);

        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);
        Result<Item> result1 = new Result<>(item1);
        Result<Item> result2 = new Result<>(item2);

        // Create a list containing the Result<Item> objects
        List<Result<Item>> resultList = Arrays.asList(result1, result2);
        byte[] jsonMessageBytes1 = jsonMessage1.getBytes(StandardCharsets.UTF_8);
        byte[] jsonMessageBytes2 = jsonMessage2.getBytes(StandardCharsets.UTF_8);
        // Mock the GetObjectResponse and its readAllBytes method
        GetObjectResponse getObjectResponse1 = mock(GetObjectResponse.class);
        GetObjectResponse getObjectResponse2 = mock(GetObjectResponse.class);

        doReturn("chat/Chatting/ChatRoom_testRoom/test1.json").when(item1).objectName();
        doReturn("chat/Chatting/ChatRoom_testRoom/test2.json").when(item2).objectName();
        doReturn(resultList).when(minioClient).listObjects(any(ListObjectsArgs.class));
        doReturn(jsonMessageBytes1).when(getObjectResponse1).readAllBytes();
        doReturn(jsonMessageBytes2).when(getObjectResponse2).readAllBytes();
        // Mock the minioClient.getObject method
        doReturn(getObjectResponse1).doReturn(getObjectResponse2)
                .when(minioClient)
                .getObject(any(GetObjectArgs.class));
        doReturn(chatMessage1).doReturn(chatMessage2)
                        .when(objectMapper)
                                .readValue(any(InputStream.class), eq(ChatMessage.class));

        List<ChatMessage> chatMessages = minioRepository.getChatMessages(bucketName, roomId);

        assertThat(chatMessages.size()).isEqualTo(2);
        assertThat(chatMessages).containsExactlyInAnyOrderElementsOf(expectedMessages);
    }

    @Test
    void getChatRooms() {
    }

    @Test
    void deleteChatRoom() {
    }
}