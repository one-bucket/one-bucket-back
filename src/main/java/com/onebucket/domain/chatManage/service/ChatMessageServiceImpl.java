package com.onebucket.domain.chatManage.service;

import com.onebucket.domain.chatManage.dao.ChatMessageRepository;
import com.onebucket.domain.chatManage.dao.ChatRoomRepository;
import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.domain.chatManage.domain.ChatRoom;
import com.onebucket.global.exceptionManage.customException.chatManageException.ChatManageException;
import com.onebucket.global.exceptionManage.customException.chatManageException.Exceptions.ChatRoomException;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import com.onebucket.global.minio.MinioRepository;
import com.onebucket.global.minio.MinioSaveInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    private final ChatRoomRepository chatRoomRepository;
    private final MinioRepository minioRepository;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.minio_url}")
    private String endpointUrl;

//    @Override
//    public void saveMessage(ChatMessage chatMessage) {
//        chatMessageRepository.save(chatMessage);
//    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessage> getChatMessages(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ChatRoomException(ChatErrorCode.NOT_EXIST_ROOM));
        return chatRoom.getMessages();
    }

    @Override
    public String uploadChatImage(MultipartFile file, String username) {
        String fileName = "chat/" + username + "/" + file.getOriginalFilename();

        MinioSaveInfoDto dto = MinioSaveInfoDto.builder()
                .bucketName(bucketName)
                .fileName(fileName)
                .fileExtension("png")
                .build();
        try {
            String address = minioRepository.uploadFile(file, dto);
            return endpointUrl + "/" + address;
        } catch (RuntimeException e) {
            throw new ChatManageException(ChatErrorCode.CHAT_IMAGE_ERROR);
        }
    }
}