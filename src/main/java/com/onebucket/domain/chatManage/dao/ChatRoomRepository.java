package com.onebucket.domain.chatManage.dao;

import com.onebucket.domain.chatManage.domain.ChatRoom;
import com.onebucket.global.minio.MinioRepository;
import com.onebucket.global.minio.MinioSaveInfoDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static com.onebucket.domain.chatManage.Const.CHAT_ROOMS;

/**
 * <br>package name   : com.onebucket.domain.chatManage.dao
 * <br>file name      : ChatRoomRepository
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
@Repository
public class ChatRoomRepository {
    private final MinioRepository minioRepository;
    private final RedisTemplate<String,Object> redisTemplate;
    private HashOperations<String,String,ChatRoom> opsHashChatRoom;

    @Value("${minio.bucketName}")
    private String bucketName;

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
    }

    public List<ChatRoom> findAllRoom() {
        return opsHashChatRoom.values(CHAT_ROOMS);
    }

    public ChatRoom findRoomById(String roomId) {
        return opsHashChatRoom.get(CHAT_ROOMS, roomId);
    }

    /**
     * 채팅방 생성 : 서버간 채팅의 공유를 위해 minio에 저장한다.
     */
    public ChatRoom createChatRoom(String name) {
        ChatRoom room = ChatRoom.builder()
                .name(name)
                .roomId(UUID.randomUUID().toString())
                .build();
        MinioSaveInfoDto dto = MinioSaveInfoDto.builder()
                .bucketName(bucketName)
                .fileName("chat/"+"ChatRoom/"+room.getRoomId())
                .fileExtension("json")
                .build();
        minioRepository.uploadChatDto(room,dto);
        return room;
    }
}
