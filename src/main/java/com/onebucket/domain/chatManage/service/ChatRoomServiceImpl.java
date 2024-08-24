package com.onebucket.domain.chatManage.service;

import com.onebucket.domain.chatManage.dao.ChatRoomRepository;
import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.domain.chatManage.domain.ChatRoom;
import com.onebucket.domain.chatManage.dto.CreateChatRoomDto;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.chatManage.dto.ChatMemberDto;
import com.onebucket.global.exceptionManage.customException.CommonException;
import com.onebucket.global.exceptionManage.customException.chatManageException.Exceptions.ChatRoomException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import com.onebucket.global.exceptionManage.errorCode.CommonErrorCode;
import com.onebucket.global.utils.ChatRoomValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public String createChatRoom(CreateChatRoomDto dto) {
        chatRoomValidator.validateCreateChatRoom(dto);
        ChatRoom chatRoom = ChatRoom.create(dto);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        return savedChatRoom.getRoomId();
    }

    @Override
    @Transactional
    public ChatRoom addChatMembers(String roomId, String username) {
        chatRoomValidator.validateChatRoomExists(roomId);

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));

        try {
            Query query = new Query(Criteria.where("roomId").is(roomId));
            Update update = new Update().addToSet("members", ChatMemberDto.from(member.getNickname()));

            return mongoTemplate.findAndModify(query, update,
                    FindAndModifyOptions.options().returnNew(true), ChatRoom.class);
        } catch (CommonException e) {
            // MongoDB 예외나 다른 예외 처리
            throw new CommonException(CommonErrorCode.DATA_ACCESS_ERROR);
        }
    }

    @Override
    @Transactional
    public ChatRoom removeChatMember(String roomId, String username) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ChatRoomException(ChatErrorCode.NOT_EXIST_ROOM));

        // 유저 존재 여부 확인
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));

        chatRoomValidator.validateMemberInChatRoom(chatRoom,member);

        try {
            // Query를 통해 roomId와 일치하는 채팅방을 찾고, members 배열에서 해당 유저를 제거
            Query query = new Query(Criteria.where("roomId").is(roomId));
            Update update = new Update().pull("members", ChatMemberDto.from(member.getNickname()));

            // findAndModify로 members에서 해당 유저를 제거

            return mongoTemplate.findAndModify(query, update,
                    FindAndModifyOptions.options().returnNew(true), ChatRoom.class);
        } catch (CommonException e) {
            // MongoDB 예외나 다른 예외 처리
            throw new CommonException(CommonErrorCode.DATA_ACCESS_ERROR);
        }
    }

    @Override
    public List<ChatRoom> getChatRooms() {
        return chatRoomRepository.findAll();
    }

    @Override
    public ChatRoom getChatRoom(String roomId) {
        return chatRoomRepository.findByRoomId(roomId).orElseThrow(
                () -> new ChatRoomException(ChatErrorCode.NOT_EXIST_ROOM,"존재하지 않는 채팅방입니다."));
    }

    @Override
    @Transactional
    public void addChatMessages(ChatMessage chatMessage) {
        chatRoomValidator.validateChatRoomExists(chatMessage.getRoomId());

        try {
            // 채팅방을 찾고, 존재하지 않으면 예외를 던짐
            Query query = new Query(Criteria.where("roomId").is(chatMessage.getRoomId()));

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
    public List<ChatRoom> findByMembersNickname(String nickname) {
        return memberRepository.existsByNickname(nickname)
                ? chatRoomRepository.findByMembersNickname(nickname)
                : Collections.emptyList();
    }

    @Override
    public void deleteChatRoom(String roomId,String username) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(
                () -> new ChatRoomException(ChatErrorCode.NOT_EXIST_ROOM));
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER)
        );

        String nickname = member.getNickname();
        String creator = chatRoom.getCreatedBy();
        chatRoomValidator.validateChatRoomCreator(nickname,creator);

        Query query = new Query(Criteria.where("roomId").is(roomId)
                .and("createdBy").is(nickname));

        mongoTemplate.remove(query, ChatRoom.class);
    }
}

