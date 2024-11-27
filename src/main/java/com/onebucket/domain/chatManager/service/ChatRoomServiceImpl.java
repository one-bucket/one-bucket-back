package com.onebucket.domain.chatManager.service;

import com.onebucket.domain.boardManage.dao.postRepository.PostRepository;
import com.onebucket.domain.boardManage.entity.post.Post;
import com.onebucket.domain.chatManager.dao.ChatRoomMemberRepository;
import com.onebucket.domain.chatManager.dao.ChatRoomRepository;
import com.onebucket.domain.chatManager.dto.ChatRoomDto;
import com.onebucket.domain.chatManager.entity.ChatRoom;
import com.onebucket.domain.chatManager.entity.ChatRoomMember;
import com.onebucket.domain.chatManager.entity.ChatRoomMemberId;
import com.onebucket.domain.chatManager.entity.TradeType;
import com.onebucket.domain.chatManager.mongo.ChatMessage;
import com.onebucket.domain.chatManager.mongo.ChatMessageRepository;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
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
    private final PostRepository postRepository;
    @Override
    public String createRoom(ChatRoomDto.CreateRoom dto) {
        String chatRoomId = UUID.randomUUID().toString();
        Member member = findMember(dto.getOwnerId());

        ChatRoom chatRoom = ChatRoom.builder()
                .id(chatRoomId)
                .ownerId(member.getId())
                .name(dto.getName())

                .tradeType(dto.getTradeType())
                .tradeId(dto.getTradeId())
                .build();

        chatRoom.addMember(member);

        chatRoomRepository.save(chatRoom);

        return chatRoomId;
    }

    @Override
    public String createAndJoinRoom(ChatRoomDto.CreateAndJoinRoom dto) {
        String chatRoomId = UUID.randomUUID().toString();

        Member owner = findMember(dto.getOwnerId());
        Member joiner = findMember(dto.getJoinerId());
        if(owner.equals(joiner)) {
            throw new ChatRoomException(ChatErrorCode.USER_NOT_CREATOR, "Creator can't join his chatRoom");
        }



        ChatRoom chatRoom = ChatRoom.builder()
                .id(chatRoomId)
                .ownerId(owner.getId())
                .name(dto.getName())

                .tradeType(dto.getTradeType())
                .tradeId(dto.getTradeId())
                .build();

        chatRoom.addMember(owner);
        chatRoom.addMember(joiner);

        chatRoomRepository.save(chatRoom);

        return chatRoomId;
    }

    @Transactional(readOnly = true)
    @Override
    public ChatRoomDto.Info getRoomDetails(String roomId) {
        ChatRoom chatRoom = findChatRoom(roomId);
        List<ChatRoomMember> roomMembers = chatRoom.getMembers();

        List<ChatRoomDto.Info.RoomMemberInfo> roomMembersInfo =
                convertChatRoomMemberInfoToDtoInternal(roomMembers);

        return ChatRoomDto.Info.builder()
                .roomId(chatRoom.getId())
                .name(chatRoom.getName())
                .ownerId(chatRoom.getOwnerId())
                .membersInfo(roomMembersInfo)
                .tradeType(chatRoom.getTradeType())
                .tradeId(chatRoom.getTradeId())
                .build();

    }

    private List<ChatRoomDto.Info.RoomMemberInfo> convertChatRoomMemberInfoToDtoInternal(List<ChatRoomMember> members) {
        return members.stream().map((chatRoomMember) -> {
            Member member = chatRoomMember.getMember();
            return ChatRoomDto.Info.RoomMemberInfo.builder()
                    .id(member.getId())
                    .nickname(member.getNickname())
                    .joinedAt(chatRoomMember.getJoinedAt())
                    .imageUrl(member.getProfile().getImageUrl())
                    .build();
        }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isMemberOfChatRoom(ChatRoomDto.ManageMember dto) {
        ChatRoomMemberId chatRoomMemberId = ChatRoomMemberId.builder()
                .chatRoom(dto.getRoomId())
                .member(dto.getMemberId())
                .build();

        return chatRoomMemberRepository.existsById(chatRoomMemberId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getMemberIds(String roomId) {
        ChatRoom chatRoom = findChatRoom(roomId);
        List<ChatRoomMember> members = chatRoom.getMembers();

        return members.stream()
                .map((chatRoomMember) -> chatRoomMember.getMember().getId())
                .toList();
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
    @Transactional(readOnly = true)
    public ChatRoomDto.ChatRoomInfo getRoomInfo(ChatRoomDto.InfoAfterTime dto) {
        ChatRoom room = findChatRoom(dto.getRoomId());
        Post post = findPost(room.getTradeId());
        List<String> images = post.getImageUrls();
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
                .imageUrl(images)
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
    @Transactional(readOnly = true)
    public LocalDateTime getDisconnectTime(ChatRoomMemberId id) {
        LocalDateTime time =  chatRoomMemberRepository.findById(id).orElseThrow(() -> new ChatRoomException(ChatErrorCode.NOT_EXIST_ROOM))
                .getDisconnectAt();
        return Objects.requireNonNullElseGet(time, () -> LocalDateTime.of(0, 1, 1, 0, 0));
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getRoomIds(Long userId) {
        return chatRoomMemberRepository.findChatRoomIdByMemberId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessage> getMessageAfterTimestamp(ChatRoomDto.InfoAfterTime dto) {
        return chatMessageRepository.findMessagesAfterTimestamp(dto.getRoomId(), dto.getTimestamp());
    }

    @Override
    @Transactional(readOnly = true)
    public ChatRoomDto.TradeIdentifier getTradeSimpleInfo(String chatRoomId) {
        ChatRoom chatRoom = findChatRoom(chatRoomId);
        TradeType tradeType = chatRoom.getTradeType();
        Long tradeId = chatRoom.getTradeId();

        return ChatRoomDto.TradeIdentifier.builder()
                .tradeId(tradeId)
                .tradeType(tradeType)
                .build();
    }

    private ChatRoom findChatRoom(String roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatRoomException(ChatErrorCode.NOT_EXIST_ROOM));
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PendingTradeException(TradeErrorCode.UNKNOWN_TRADE));
    }


}
