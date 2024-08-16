package com.onebucket.domain.chatManage.service;

import com.onebucket.domain.chatManage.dao.ChatRoomRepository;
import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.domain.chatManage.domain.ChatRoom;
import com.onebucket.domain.chatManage.dto.CreateChatRoomDto;
import com.onebucket.domain.chatManage.pubsub.RedisSubscriber;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.chatManage.dto.ChatMemberDto;
import com.onebucket.global.exceptionManage.customException.CommonException;
import com.onebucket.global.exceptionManage.customException.chatManageException.Exceptions.RoomNotFoundException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import com.onebucket.global.exceptionManage.errorCode.CommonErrorCode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

    @Override
    public String createChatRoom(CreateChatRoomDto dto) {
        ChatRoom chatRoom = ChatRoom.builder()
                .name(dto.name())
                .createdBy(dto.createdBy())
                .createdAt(dto.createdAt())
                .members(dto.members())
                .build();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        return savedChatRoom.getRoomId();
    }

    @Override
    @Transactional
    public void addChatMembers(String roomId, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));

        Query query = new Query(Criteria.where("roomId").is(roomId));
        Update update = new Update().addToSet("members", ChatMemberDto.from(member.getNickname()));

        ChatRoom updatedChatRoom = mongoTemplate.findAndModify(query, update,
                FindAndModifyOptions.options().returnNew(true), ChatRoom.class);

        if (updatedChatRoom == null) {
            throw new RoomNotFoundException(ChatErrorCode.NOT_EXIST_ROOM);
        }
    }

    @Override
    public List<ChatRoom> getChatRooms() {
        return chatRoomRepository.findAll();
    }

    @Override
    public ChatRoom getChatRoom(String roomId) {
        return chatRoomRepository.findByRoomId(roomId).orElseThrow(
                () -> new RoomNotFoundException(ChatErrorCode.NOT_EXIST_ROOM));
    }

    @Override
    @Transactional
    public void addChatMessages(ChatMessage chatMessage) {
        if (chatMessage.getRoomId() == null) {
            throw new RoomNotFoundException(ChatErrorCode.NOT_EXIST_ROOM);
        }

        try {
            // 채팅방을 찾고, 존재하지 않으면 예외를 던짐
            Query query = new Query(Criteria.where("roomId").is(chatMessage.getRoomId()));

            // 채팅 메시지를 추가하는 업데이트 정의
            Update update = new Update().push("messages", chatMessage);

            // findAndModify를 사용하여 원자적으로 메시지를 추가하고, 업데이트된 채팅방을 반환
            ChatRoom updatedChatRoom = mongoTemplate.findAndModify(query, update,
                    FindAndModifyOptions.options().returnNew(true), ChatRoom.class);

            // 업데이트된 채팅방이 없으면 (즉, 채팅방이 존재하지 않으면) 예외 던짐
            if (updatedChatRoom == null) {
                throw new RoomNotFoundException(ChatErrorCode.NOT_EXIST_ROOM);
            }
        } catch (Exception e) {
            // MongoDB 예외나 다른 예외 처리
            throw new CommonException(CommonErrorCode.DATA_ACCESS_ERROR);
        }
    }


    @Override
    public List<ChatRoom> findByMembersNickname(String nickname) {
        return memberRepository.existsByNickname(nickname)
                ? chatRoomRepository.findByMembersNickname(nickname)
                : Collections.emptyList();
    }
}

