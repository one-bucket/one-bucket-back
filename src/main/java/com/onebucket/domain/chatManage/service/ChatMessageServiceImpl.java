package com.onebucket.domain.chatManage.service;

import com.onebucket.domain.chatManage.dao.ChatMessageRepository;
import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.global.minio.MinioRepository;
import com.onebucket.global.minio.MinioSaveInfoDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.onebucket.domain.chatManage.Const.CHAT_MESSAGE_ORIGIN;

/**
 * <br>package name   : com.onebucket.domain.chatManage.service
 * <br>file name      : ChatMessageServiceImpl
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
@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    @Override
    public void saveMessage(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessage> getChatMessages(String roomId) {
        return chatMessageRepository.findByRoomIdOrderByUpdatedAtDesc(roomId);
    }
}