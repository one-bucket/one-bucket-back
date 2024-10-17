package com.onebucket.domain.chatManager.service;

import com.onebucket.domain.chatManager.dao.ChatRoomMemberRepository;
import com.onebucket.domain.chatManager.dao.ChatRoomRepository;
import com.onebucket.domain.chatManager.dto.ChatRoomDto;
import com.onebucket.domain.chatManager.entity.ChatRoom;
import com.onebucket.domain.chatManager.entity.ChatRoomMember;
import com.onebucket.domain.chatManager.entity.ChatRoomMemberId;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.tradeManage.dao.PendingTradeRepository;
import com.onebucket.domain.tradeManage.entity.PendingTrade;
import com.onebucket.global.exceptionManage.customException.TradeManageException.PendingTradeException;
import com.onebucket.global.exceptionManage.customException.chatManageException.Exceptions.ChatRoomException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import com.onebucket.global.exceptionManage.errorCode.TradeErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;
import java.util.UUID;

/**
 * <br>package name   : com.onebucket.domain.chatManager.service
 * <br>file name      : ChatRoomServiceImpl
 * <br>date           : 2024-10-17
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 */

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final PendingTradeRepository pendingTradeRepository;
    private final MemberRepository memberRepository;

    @Override
    public boolean existsById(String roomId) {
        return chatRoomRepository.existsById(roomId);
    }

    @Override
    public boolean isMemberOfChatRoom(ChatRoomDto.ManageMember dto) {
        ChatRoomMemberId chatRoomMemberId = ChatRoomMemberId.builder()
                .chatRoom(dto.getRoomId())
                .member(dto.getMemberId())
                .build();

        return chatRoomMemberRepository.existsById(chatRoomMemberId);
    }

    @Override
    public List<ChatRoomDto.MemberInfo> getMemberList(String roomId) {
        ChatRoom chatRoom = findChatRoom(roomId);
        List<ChatRoomMember> members = chatRoom.getMembers();

        return members.stream()
                .map(ChatRoomDto.MemberInfo::of).toList();
    }

    @Override
    @Transactional
    public String createRoom(ChatRoomDto.CreateRoom dto) {
        String id = UUID.randomUUID().toString();
//        PendingTrade pendingTrade = pendingTradeRepository.findById(dto.getTradeId())
//                .orElseThrow(() -> new PendingTradeException(TradeErrorCode.UNKNOWN_TRADE));
        Member member = findMember(dto.getMemberId());

        ChatRoom chatRoom = ChatRoom.builder()
                .id(id)
                .name(dto.getName())
               // .pendingTrade(pendingTrade)
                .build();

        chatRoom.addMember(member);

        chatRoomRepository.save(chatRoom);
        return id;
    }

    @Override
    public ChatRoomDto.GetTradeInfo getTradeInfo(String roomId) {
        ChatRoom chatRoom = findChatRoom(roomId);

        PendingTrade pendingTrade = chatRoom.getPendingTrade();
        if(pendingTrade == null) {
            throw new PendingTradeException(TradeErrorCode.UNKNOWN_TRADE);
        }

        List<ChatRoomDto.MemberInfo> members = chatRoom.getMembers().stream().map(ChatRoomDto.MemberInfo::of).toList();

        ChatRoomDto.GetTradeInfo info =  ChatRoomDto.GetTradeInfo.of(pendingTrade);
        info.setMemberList(members);

        return info;
    }

    @Override
    public void changeRoomName(ChatRoomDto.ChangeRoomName dto) {
        ChatRoom chatRoom = findChatRoom(dto.getRoomId());
        chatRoom.setName(dto.getName());
        chatRoomRepository.save(chatRoom);
    }

    @Override
    public void deleteRoom(String roomId) {
        chatRoomRepository.deleteById(roomId);
    }

    @Override
    public Long addMember(ChatRoomDto.ManageMember dto) {
        Member member = findMember(dto.getMemberId());
        ChatRoom chatRoom = findChatRoom(dto.getRoomId());

        chatRoom.addMember(member);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        int memberCount = savedChatRoom.getMembers().size();
        return (long) memberCount;
    }

    @Override
    public Long quitMember(ChatRoomDto.ManageMember dto) {
        Member member = findMember(dto.getMemberId());
        ChatRoom chatRoom = findChatRoom(dto.getRoomId());

        chatRoom.quitMember(member);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        int memberCount = savedChatRoom.getMembers().size();
        return (long) memberCount;
    }

    private ChatRoom findChatRoom(String roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatRoomException(ChatErrorCode.NOT_EXIST_ROOM));
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));
    }
}
