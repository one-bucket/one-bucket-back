package com.onebucket.domain.chatManage.dao;

import com.onebucket.domain.chatManage.domain.ChatMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <br>package name   : com.onebucket.domain.chatManage.dao
 * <br>file name      : ChatMessageRepositoryTest
 * <br>date           : 2024-08-06
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
 * 2024-08-06        SeungHoon              init create
 * </pre>
 */
@DataMongoTest
@MockBean(JpaMetamodelMappingContext.class)
class ChatMessageRepositoryTest {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @BeforeEach
    void setUp() {
        chatMessageRepository.deleteAll();

        ChatMessage message1 = new ChatMessage();
        message1.setRoomId("room1");
        message1.setMessage("Hello");
        message1.setCreatedAt(LocalDateTime.now().minusHours(1));

        ChatMessage message2 = new ChatMessage();
        message2.setRoomId("room1");
        message2.setMessage("Hi");
        message2.setCreatedAt(LocalDateTime.now());

        chatMessageRepository.save(message1);
        chatMessageRepository.save(message2);
    }

    @Test
    @DisplayName("roomId를 기반으로 최신 게시물 순으로 조회하기")
    void FindByRoomIdOrderByCreatedAtDesc_success() {
        List<ChatMessage> messages = chatMessageRepository.findByRoomIdOrderByCreatedAtDesc("room1");

        assertThat(messages).hasSize(2);
        assertThat(messages.get(0).getMessage()).isEqualTo("Hi");
        assertThat(messages.get(1).getMessage()).isEqualTo("Hello");
    }
}