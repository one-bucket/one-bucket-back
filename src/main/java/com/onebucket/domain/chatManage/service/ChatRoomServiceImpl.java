package com.onebucket.domain.chatManage.service;

import com.onebucket.domain.chatManage.dao.ChatRoomRepository;
import com.onebucket.domain.chatManage.domain.ChatRoom;
import com.onebucket.domain.chatManage.dto.chatmessage.ChatMessageDto;
import com.onebucket.domain.chatManage.dto.chatroom.CreateChatRoomDto;
import com.onebucket.domain.chatManage.dto.chatroom.ResponseChatRoomListDto;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.chatManage.dto.chatroom.ChatMemberDto;
import com.onebucket.global.exceptionManage.customException.CommonException;
import com.onebucket.global.exceptionManage.customException.chatManageException.ChatManageException;
import com.onebucket.global.exceptionManage.customException.chatManageException.Exceptions.ChatRoomException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import com.onebucket.global.exceptionManage.errorCode.CommonErrorCode;
import com.onebucket.global.minio.MinioRepository;
import com.onebucket.global.minio.MinioSaveInfoDto;
import com.onebucket.global.utils.ChatRoomValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * <br>package name   : com.onebucket.domain.chatManage.service
 * <br>file name      : ChatRoomServiceImpl
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
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final MemberRepository memberRepository;

    private final ChatRoomRepository chatRoomRepository;

    private final MongoTemplate mongoTemplate;

    private final ChatRoomValidator chatRoomValidator;

    private final MinioRepository minioRepository;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.minio_url}")
    private String endpointUrl;

    @Override
    public String createChatRoom(CreateChatRoomDto dto) {
        chatRoomValidator.validateCreateChatRoom(dto);
        ChatRoom chatRoom = ChatRoom.create(dto);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        return savedChatRoom.getRoomId();
    }

    @Override
    @Transactional
    public void addChatMembers(String roomId, String nickname) {
        chatRoomValidator.validateChatRoomExists(roomId);

        if(!memberRepository.existsByNickname(nickname)) {
            throw new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER);
        }

        try {
            Query query = new Query(Criteria.where("roomId").is(roomId));
            Update update = new Update().addToSet("members", ChatMemberDto.from(nickname));

            mongoTemplate.findAndModify(query, update,
                    FindAndModifyOptions.options().returnNew(true), ChatRoom.class);
        } catch (CommonException e) {
            // MongoDB 예외나 다른 예외 처리
            throw new CommonException(CommonErrorCode.DATA_ACCESS_ERROR);
        }
    }

    @Override
    @Transactional
    public void removeChatMember(String roomId, String nickname) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ChatRoomException(ChatErrorCode.NOT_EXIST_ROOM));

        // 유저 존재 여부 확인
        if(!memberRepository.existsByNickname(nickname)) {
            throw new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER);
        }

        chatRoomValidator.validateMemberInChatRoom(chatRoom,nickname);

        try {
            // Query를 통해 roomId와 일치하는 채팅방을 찾고, members 배열에서 해당 유저를 제거
            Query query = new Query(Criteria.where("roomId").is(roomId));
            Update update = new Update().pull("members", ChatMemberDto.from(nickname));

            // findAndModify로 members에서 해당 유저를 제거
            mongoTemplate.findAndModify(query, update,
                    FindAndModifyOptions.options().returnNew(true), ChatRoom.class);
        } catch (CommonException e) {
            // MongoDB 예외나 다른 예외 처리
            throw new CommonException(CommonErrorCode.DATA_ACCESS_ERROR);
        }
    }

    @Override
    public List<ResponseChatRoomListDto> getChatRooms() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        return chatRooms.stream()
                .map(ResponseChatRoomListDto::from)
                .toList();
    }

    @Override
    @Transactional
    public void addChatMessages(ChatMessageDto chatMessage) {
        chatRoomValidator.validateChatRoomExists(chatMessage.roomId());

        try {
            // 채팅방을 찾고, 존재하지 않으면 예외를 던짐
            Query query = new Query(Criteria.where("roomId").is(chatMessage.roomId()));

            // 채팅 메시지를 추가하는 업데이트 정의
            Update update = new Update().push("messages", chatMessage);

            // findAndModify를 사용하여 원자적으로 메시지를 추가하고, 업데이트된 채팅방을 반환
            mongoTemplate.findAndModify(query, update,
                    FindAndModifyOptions.options().returnNew(true), ChatRoom.class);
        } catch (CommonException e) {
            // MongoDB 예외나 다른 예외 처리
            throw new CommonException(CommonErrorCode.DATA_ACCESS_ERROR);
        }
    }


    @Override
    public List<ResponseChatRoomListDto> findByMembersNickname(String nickname) {
        if (!memberRepository.existsByNickname(nickname)){
            return Collections.emptyList();
        }
        List<ChatRoom> chatRooms = chatRoomRepository.findByMembersNickname(nickname);
        return chatRooms.stream()
                .map(ResponseChatRoomListDto::from)
                .toList();
    }

    @Override
    public void deleteChatRoom(String roomId,String username) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(
                () -> new ChatRoomException(ChatErrorCode.NOT_EXIST_ROOM));
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));

        String nickname = member.getNickname();
        String creator = chatRoom.getCreatedBy();
        chatRoomValidator.validateChatRoomCreator(nickname,creator);

        Query query = new Query(Criteria.where("roomId").is(roomId)
                .and("createdBy").is(nickname));

        mongoTemplate.remove(query, ChatRoom.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageDto> getChatMessages(String roomId) {
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

