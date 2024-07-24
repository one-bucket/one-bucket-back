package com.onebucket.global.minio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onebucket.domain.chatManage.domain.ChatRoom;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.onebucket.domain.chatManage.domain.ChatMessage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * <br>package name   : com.onebucket.global.minio
 * <br>file name      : MinioRepository
 * <br>date           : 2024-07-02
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
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-07-02        jack8              init create
 * </pre>
 */

@Repository
@RequiredArgsConstructor
public class MinioRepository {

     private final MinioClient minioClient;
     private final ObjectMapper objectMapper;


     public String uploadFile(MultipartFile multipartFile, MinioSaveInfoDto dto) {
         try {
             boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder()
                     .bucket(dto.getBucketName()).build());
             if(!isExist) {
                 minioClient.makeBucket(MakeBucketArgs.builder().bucket(dto.getBucketName()).build());
                 minioClient.setBucketPolicy(
                         SetBucketPolicyArgs.builder()
                                 .bucket(dto.getBucketName())
                                 .config("public")
                                 .build()
                 );
             }

             try(InputStream inputStream = multipartFile.getInputStream()) {
                 minioClient.putObject(
                         PutObjectArgs.builder()
                                 .bucket(dto.getBucketName())
                                 .object(dto.getFileName() + "." + dto.getFileExtension())
                                 .stream(inputStream, multipartFile.getSize(), -1)
                                 .contentType(multipartFile.getContentType())
                                 .build()
                 );
             }


             return dto.getBucketName() + "/" + dto.getFileName() + "." + dto.getFileExtension();

         } catch(Exception e) {
             // TODO: make this as custom.
             throw new RuntimeException("Error occur : " + e.getMessage());
         }
     }

     public byte[] getFile(MinioSaveInfoDto dto) {
         try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
             try (InputStream stream = minioClient.getObject(
                     GetObjectArgs.builder()
                             .bucket(dto.getBucketName())
                             .object(dto.getFileName() + "." + dto.getFileExtension())
                             .build())) {
                 byte[] buffer = new byte[1024];
                 int byteRead;
                 while((byteRead = stream.read(buffer)) != -1) {
                     baos.write(buffer, 0, byteRead);
                 }
                 return baos.toByteArray();
             }
         }catch(MinioException | IOException e) {
             throw new RuntimeException("Error occurred while fetching files:" + e.getMessage());
         } catch(Exception e) {
             throw new RuntimeException("Unknown Exception in MinioRepository.class : " + e.getMessage());
         }
     }

    /**
     * minio에 채팅 메세지를 저장함.
     * @param object
     * @param dto
     */
    public void uploadChatDto(Object object, MinioSaveInfoDto dto) {
        try {
            makeBucket(dto);

            // 채팅 메시지를 JSON 형식으로 변환
            String jsonMessage = objectMapper.writeValueAsString(object);
            byte[] jsonBytes = jsonMessage.getBytes(StandardCharsets.UTF_8);

            try (InputStream inputStream = new ByteArrayInputStream(jsonBytes)) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(dto.getBucketName())
                                .object(dto.getFileName() + "." + dto.getFileExtension())
                                .stream(inputStream, jsonBytes.length, -1)
                                .contentType("application/json")
                                .build()
                );
            }
        } catch(Exception e) {
            throw new RuntimeException("Error occur : " + e.getMessage());
        }
    }

    private void makeBucket(MinioSaveInfoDto dto) throws ErrorResponseException, InsufficientDataException, InternalException, InvalidKeyException, InvalidResponseException, IOException, NoSuchAlgorithmException, ServerException, XmlParserException {
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(dto.getBucketName()).build());
        if(!isExist) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(dto.getBucketName()).build());
            minioClient.setBucketPolicy(
                    SetBucketPolicyArgs.builder()
                            .bucket(dto.getBucketName())
                            .config("public")
                            .build()
            );
        }
    }

    /**
     * minio에서 채팅메세지를 가져옴.
     * @param bucketName
     * @param roomId
     * @return
     */
    public List<ChatMessage> getChatMessages(String bucketName, String roomId) {
        List<ChatMessage> chatMessages = new ArrayList<>();
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix("chat/Chatting/ChatRoom_"+ roomId + "/")
                            .build());

            for (Result<Item> result : results) {
                Item item = result.get();
                try (InputStream stream = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(bucketName)
                                .object(item.objectName())
                                .build())) {
                    ChatMessage chatMessage = objectMapper.readValue(stream, ChatMessage.class);
                    chatMessages.add(chatMessage);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching chat messages: " + e.getMessage());
        }
        return chatMessages;
    }

    /**
     * 채팅을 저장하는 과정에서 생성되는 폴더가 채팅방의 역할을 한다. 그렇다면 굳이 내가 따로 채팅방을 저장해야 하는 이유가 있는가?
     * 채팅방에 기능이 좀 더 필요하면 따로 저장을 해야하는 가치가 있을지도 모르겠다. 당장 해당 채팅방에 위치한 유저들을 출력하려고 해도 필요한듯.
     * @param bucketName
     * @return
     */
    public List<ChatRoom> getChatRooms(String bucketName) {
        List<ChatRoom> chatRooms = new ArrayList<>();
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix("chat/ChatRoom/")
                            .build());

            for (Result<Item> result : results) {
                Item item = result.get();
                try (InputStream stream = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(bucketName)
                                .object(item.objectName())
                                .build())) {
                    ChatRoom chatroom = objectMapper.readValue(stream, ChatRoom.class);
                    chatRooms.add(chatroom);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching chat rooms: " + e.getMessage());
        }
        return chatRooms;
    }

    public void deleteChatRoom(String bucketName, String roomId) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object("chat/ChatRoom_/" + roomId + ".json")
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while deleting chat room: " + e.getMessage());
        }
    }
}
