package com.onebucket.domain.chatManage.service;

import com.onebucket.domain.chatManage.dao.ChatRoomRepository;
import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.domain.chatManage.domain.ChatRoom;
import com.onebucket.domain.chatManage.dto.CreateChatRoomDto;
import com.onebucket.domain.chatManage.pubsub.RedisSubscriber;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.chatManage.dto.ChatMemberDto;
import com.onebucket.global.exceptionManage.customException.chatManageException.Exceptions.RoomNotFoundException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
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
    public void enterChatRoom(String roomId, String username) {
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
    public void addChatMessage(ChatMessage chatMessage) {
        // Find the chat room by roomId
        Query query = new Query(Criteria.where("roomId").is(chatMessage.getRoomId()));

        // Update the chat room by adding the message to the messages list
        Update update = new Update().push("messages", chatMessage);

        // Perform the atomic findAndModify operation
        ChatRoom updatedChatRoom = mongoTemplate.findAndModify(query, update,
                FindAndModifyOptions.options().returnNew(true), ChatRoom.class);

        if (updatedChatRoom == null) {
            throw new RoomNotFoundException(ChatErrorCode.NOT_EXIST_ROOM);
        }
    }

    @Override
    public List<ChatRoom> findByMembersNickname(String nickname) {
        return memberRepository.existsByNickname(nickname)
                ? chatRoomRepository.findByMembersNickname(nickname)
                : Collections.emptyList();
    }
}

