package com.onebucket.domain.chatManage.dao;

import com.onebucket.domain.chatManage.domain.ChatRoom;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * <br>package name   : com.onebucket.domain.chatManage.dao
 * <br>file name      : ChatRoomRepositoryTest
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
class ChatRoomRepositoryTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @BeforeEach
    void setUp() {
        chatRoomRepository.deleteAll();

       ChatRoom chatRoom = ChatRoom.builder()
               .name("room1")
               .createdAt(LocalDateTime.of(1,1,1,1,1))
               .createdBy("user1")
               .build();

       chatRoomRepository.save(chatRoom);
    }

//    @Test
//    @DisplayName("RoomId로 방 찾기 성공")
//    void findByRoomId_success() {
//        ChatRoom findChatRoom = chatRoomRepository.findByRoomId("1").orElseThrow(
//                () -> new RoomNotFoundException(ChatErrorCode.NOT_EXIST_ROOM));
//
//        assertThat(findChatRoom.getRoomId()).isEqualTo("1");
//        assertThat(findChatRoom.getName()).isEqualTo("room1");
//        assertThat(findChatRoom.getCreatedAt()).isEqualTo(LocalDateTime.of(1,1,1,1,1));
//        assertThat(findChatRoom.getCreatedBy()).isEqualTo("user1");
//    }
//
//    @Test
//    @DisplayName("RoomId로 방 찾기 실패 - 없는 roomId")
//    void findByRoomId_fail() {
//        Optional<ChatRoom> findChatRoom = chatRoomRepository.findByRoomId("2");
//
//        assertThat(findChatRoom).isEmpty();
//    }
}