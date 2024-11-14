package com.onebucket.domain.chatManager.service;

import com.onebucket.domain.chatManager.dao.ChatRoomMemberRepository;
import com.onebucket.domain.chatManager.dao.ChatRoomRepository;
import com.onebucket.domain.chatManager.dto.ChatRoomDto;
import com.onebucket.domain.chatManager.entity.ChatRoom;
import com.onebucket.domain.chatManager.entity.ChatRoomMember;
import com.onebucket.domain.chatManager.entity.ChatRoomMemberId;
import com.onebucket.domain.chatManager.mongo.ChatMessage;
import com.onebucket.domain.chatManager.mongo.ChatMessageRepository;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.tradeManage.dao.pendingTrade.GroupTradeRepository;
import com.onebucket.domain.tradeManage.dto.TradeDto;
import com.onebucket.domain.tradeManage.entity.GroupTrade;
import com.onebucket.global.exceptionManage.customException.TradeManageException.PendingTradeException;
import com.onebucket.global.exceptionManage.customException.chatManageException.Exceptions.ChatRoomException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import com.onebucket.global.exceptionManage.errorCode.TradeErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
@Transactional
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MemberRepository memberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final GroupTradeRepository groupTradeRepository;

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
    public String createRoom(ChatRoomDto.CreateRoom dto) {
        String id = UUID.randomUUID().toString();
        GroupTrade groupTrade = groupTradeRepository.findById(dto.getTradeId())
                .orElseThrow(() -> new PendingTradeException(TradeErrorCode.UNKNOWN_TRADE));
        Member member = findMember(dto.getMemberId());

        ChatRoom chatRoom = ChatRoom.builder()
                .id(id)
                .ownerId(member.getId())
                .name(dto.getName())
                .build();

        chatRoom.addMember(member);

        groupTrade.setChatRoom(chatRoom);
        groupTradeRepository.save(groupTrade);
        return id;
    }

    @Override
    public ChatRoomDto.GetTradeInfo getTradeInfo(String roomId) {
        ChatRoom chatRoom = findChatRoom(roomId);

        GroupTrade groupTrade = chatRoom.getGroupTrade();
        if(groupTrade == null) {
            throw new PendingTradeException(TradeErrorCode.UNKNOWN_TRADE);
        }

        List<ChatRoomDto.MemberInfo> members = chatRoom.getMembers().stream().map(ChatRoomDto.MemberInfo::of).toList();

        ChatRoomDto.GetTradeInfo info =  ChatRoomDto.GetTradeInfo.of(groupTrade);
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
    @Transactional
    public Long addMember(ChatRoomDto.ManageMember dto) {
        Member member = findMember(dto.getMemberId());
        ChatRoom chatRoom = findChatRoom(dto.getRoomId());

        chatRoom.addMember(member);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        int memberCount = savedChatRoom.getMembers().size();
        return (long) memberCount;
    }

    @Override
    @Transactional
    public void quitMember(ChatRoomDto.ManageMember dto) {
        ChatRoomMemberId chatRoomMemberId = ChatRoomMemberId.builder()
                .chatRoom(dto.getRoomId())
                .member(dto.getMemberId())
                .build();

        chatRoomMemberRepository.deleteById(chatRoomMemberId);
    }

    @Override
    @Transactional
    public ChatRoomDto.ChatRoomInfo getRoomInfo(ChatRoomDto.InfoAfterTime dto) {
        ChatRoom room = findChatRoom(dto.getRoomId());
        String roomName = room.getName();
        Long memberCount = (long) room.getMembers().size();
        Long stackMessage = chatMessageRepository.countMessagesAfterTimestamp(dto.getRoomId(), dto.getTimestamp());
        Long ownerId = room.getOwnerId();
        ChatMessage recentMessage = chatMessageRepository.findTopByRoomIdOrderByTimestampDesc(dto.getRoomId());
        if(recentMessage == null) {
            recentMessage = ChatMessage.builder()
                    .message(null)
                    .sender(null)
                    .id(dto.getRoomId())
                    .timestamp(null)
                    .build();
        }
        String recentMessageText = recentMessage.getMessage();
        Date recentMessageTime = recentMessage.getTimestamp();

        return ChatRoomDto.ChatRoomInfo.builder()
                .roomId(room.getId())
                .roomName(roomName)
                .memberCount(memberCount)
                .stackMessage(stackMessage)
                .recentMessage(recentMessageText)
                .recentMessageTime(recentMessageTime)
                .ownerId(ownerId)
                .build();
    }

    @Override
    @Transactional
    public void setDisconnectTime(ChatRoomDto.SetDisconnectTime dto) {
        ChatRoomMemberId id = ChatRoomMemberId.builder()
                .member(dto.getUserId())
                .chatRoom(dto.getRoomId())
                .build();
        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findById(id).orElseThrow(() ->
                new ChatRoomException(ChatErrorCode.NOT_EXIST_ROOM));

        chatRoomMember.setDisconnectAt(dto.getDisconnectAt());
        chatRoomMemberRepository.save(chatRoomMember);
    }

    @Override
    @Transactional
    public LocalDateTime getDisconnectTime(ChatRoomMemberId id) {
        LocalDateTime time =  chatRoomMemberRepository.findById(id).orElseThrow(() -> new ChatRoomException(ChatErrorCode.NOT_EXIST_ROOM))
                .getDisconnectAt();
        return Objects.requireNonNullElseGet(time, () -> LocalDateTime.of(0, 1, 1, 0, 0));
    }

    @Override
    @Transactional
    public List<String> getRoomIds(Long userId) {
        return chatRoomMemberRepository.findChatRoomIdByMemberId(userId);
    }

    @Override
    @Transactional
    public List<ChatMessage> getMessageAfterTimestamp(ChatRoomDto.InfoAfterTime dto) {
        return chatMessageRepository.findMessagesAfterTimestamp(dto.getRoomId(), dto.getTimestamp());
    }

    @Override
    @Transactional
    public TradeDto.Info getTradeInfoOfChatRoom(String chatRoomId) {
        ChatRoom chatRoom = findChatRoom(chatRoomId);
        GroupTrade groupTrade = chatRoom.getGroupTrade();
        if(groupTrade != null) {
            return TradeDto.Info.of(groupTrade);
        } else {
            throw new PendingTradeException(TradeErrorCode.UNKNOWN_TRADE);
        }
    }

    @Transactional
    @Override
    public void bombRoomByOwner(ChatRoomDto.ManageMember dto) {
        ChatRoom chatRoom = findChatRoom(dto.getRoomId());
        GroupTrade groupTrade = chatRoom.getGroupTrade();
        if(chatRoom.getOwnerId().equals(dto.getMemberId())) {
            groupTradeRepository.delete(groupTrade);
        }
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
