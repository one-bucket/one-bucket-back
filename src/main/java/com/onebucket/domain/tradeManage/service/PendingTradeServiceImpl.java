package com.onebucket.domain.tradeManage.service;

import com.onebucket.domain.chatManager.dao.ChatRoomMemberRepository;
import com.onebucket.domain.chatManager.dao.ChatRoomRepository;
import com.onebucket.domain.chatManager.entity.ChatRoom;
import com.onebucket.domain.chatManager.entity.ChatRoomMemberId;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.tradeManage.dao.closedTrade.ClosedGroupTradeRepository;
import com.onebucket.domain.tradeManage.dao.pendingTrade.GroupTradeRepository;
import com.onebucket.domain.tradeManage.dao.TradeTagRepository;
import com.onebucket.domain.tradeManage.dto.TradeDto;
import com.onebucket.domain.tradeManage.dto.TradeKeyDto;
import com.onebucket.domain.tradeManage.entity.ClosedGroupTrade;
import com.onebucket.domain.tradeManage.entity.GroupTrade;
import com.onebucket.domain.tradeManage.entity.TradeTag;
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
import java.util.List;


/**
 * <br>package name   : com.onebucket.domain.tradeManage.service
 * <br>file name      : PendingTradeServiceImpl
 * <br>date           : 2024-09-24
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
@Transactional
@RequiredArgsConstructor
public class PendingTradeServiceImpl
        implements PendingTradeService {

    private final GroupTradeRepository groupTradeRepository;
    private final TradeTagRepository tradeTagRepository;
    private final ClosedGroupTradeRepository closedGroupTradeRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Override
    public Long create(TradeDto.Create dto) {
        Member member = findMember(dto.getOwnerId());
        TradeTag tag = findTag(dto.getTag());

        GroupTrade groupTrade = TradeDto.to(dto, member, tag);

        return groupTradeRepository.save(groupTrade).getId();
    }
    @Override
    public TradeDto.Info getInfo(Long tradeId) {

        GroupTrade groupTrade = findPendingTrade(tradeId);

        return TradeDto.Info.of(groupTrade);
    }
    @Override
    public void update(TradeDto.Update dto) {
        GroupTrade groupTrade = findPendingTrade(dto.getTradeId());
        TradeTag tag = findTag(dto.getTag());

        groupTrade.setItem(dto.getItem());
        groupTrade.setWanted(dto.getWanted());
        groupTrade.setPrice(dto.getPrice());
        groupTrade.setCount(dto.getCount());
        groupTrade.setLocation(dto.getLocation());
        groupTrade.setLinkUrl(dto.getLinkUrl());
        groupTrade.setTradeTag(tag);

        groupTradeRepository.save(groupTrade);
    }
    @Override
    //새로운 유저가 해당 거래에 참여
    public TradeDto.ResponseJoinTrade addMember(TradeKeyDto.UserTrade dto) {
        Long userId = dto.getUserId();
        Long tradeId = dto.getTradeId();

        Member member = findMember(userId);
        GroupTrade groupTrade = findPendingTrade(tradeId);

        List<Member> joiners = groupTrade.getJoiners();
        Long wanted = groupTrade.getWanted();
        LocalDateTime dueDate = groupTrade.getDueDate();
        Member owner = groupTrade.getOwner();

        if(joiners.size() >= wanted) {
            throw new PendingTradeException(TradeErrorCode.FULL_TRADE);
        }
        if(member == owner || joiners.contains(member)) {
            throw new PendingTradeException(TradeErrorCode.ALREADY_JOIN);
        }
        if(groupTrade.isFin()) {
            throw new PendingTradeException(TradeErrorCode.FINISH_TRADE);
        }
        if(dueDate.isBefore(LocalDateTime.now())) {
            throw new PendingTradeException(TradeErrorCode.DUE_DATE_OVER);
        }

        groupTrade.addMember(member);

        //chatRoom에 멤버 추가
        ChatRoom chatRoom = groupTrade.getChatRoom();
        if(chatRoom == null) {
            throw new ChatRoomException(ChatErrorCode.NOT_EXIST_ROOM);
        }
        chatRoom.addMember(member);

        groupTradeRepository.save(groupTrade);

        return TradeDto.ResponseJoinTrade.builder()
                .tradeId(tradeId)
                .chatRoomId(chatRoom.getId())
                .build();
    }

    //참여해 있던 유저가 탈퇴
    @Override
    public void quitMember(TradeKeyDto.UserTrade dto) {
        Long userId = dto.getUserId();
        Long tradeId = dto.getTradeId();

        Member member = findMember(userId);
        GroupTrade groupTrade = findPendingTrade(tradeId);
        if(groupTrade.getOwner().equals(member)) {
            throw new PendingTradeException(TradeErrorCode.OWNER_CANNOT_QUIT);
        }
        groupTrade.deleteMember(member);

        //chatRoom에서 삭제
        String chatRoomId = groupTrade.getChatRoom().getId();
        ChatRoomMemberId chatRoomMemberId = ChatRoomMemberId.builder()
                .member(userId)
                .chatRoom(chatRoomId)
                .build();
        chatRoomMemberRepository.deleteById(chatRoomMemberId);
        groupTradeRepository.save(groupTrade);
    }
    @Override
    public boolean makeFinish(TradeKeyDto.Finish dto) {
        Long tradeId = dto.getTradeId();
        boolean isFin = dto.isFin();
        GroupTrade groupTrade = findPendingTrade(tradeId);

        groupTrade.setFin(isFin);

        GroupTrade savedGroupTrade = groupTradeRepository.save(groupTrade);
        return savedGroupTrade.isFin();
    }

    //TODO: 종료 프로토콜 좀 더 다듬어봐야 할듯
    @Override
    public Long terminate(TradeKeyDto.FindTrade dto) {
        GroupTrade groupTrade = findPendingTrade(dto.getTradeId());

        groupTradeRepository.delete(groupTrade);
        return closedGroupTradeRepository.save((ClosedGroupTrade) groupTrade).getId();
    }
    @Override
    public LocalDateTime extendDueDate(TradeKeyDto.ExtendDate dto) {
        GroupTrade groupTrade = findPendingTrade(dto.getTradeId());

        groupTrade.extendDueDate(dto.getDate());

        return groupTradeRepository.save(groupTrade).getDueDate();
    }

    @Override
    public void setChatRoom(TradeKeyDto.SettingChatRoom dto) {
        GroupTrade groupTrade = findPendingTrade(dto.getTradeId());
        ChatRoom chatRoom = findChatRoom(dto.getChatRoomId());

        groupTrade.setChatRoom(chatRoom);

        groupTradeRepository.save(groupTrade);
    }

    public List<Long> getJoinedTradeExceptOwner(Long userId) {
        return groupTradeRepository.findPendingTradeIdsWhereUserIsParticipant(userId);
    }

    private Member findMember(Long userId) {
        return memberRepository.findById(userId).orElseThrow(() ->
                new AuthenticationException((AuthenticationErrorCode.NON_EXIST_AUTHENTICATION)));
    }


    private GroupTrade findPendingTrade(Long tradeId) {
        return groupTradeRepository.findById(tradeId).orElseThrow(() ->
                new PendingTradeException(TradeErrorCode.UNKNOWN_TRADE));
    }
    private TradeTag findTag(String tag) {
        return tradeTagRepository.findById(tag).orElseThrow(() ->
                new PendingTradeException(TradeErrorCode.UNKNOWN_TAG));
    }

    private ChatRoom findChatRoom(String chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElseThrow(() ->
                new ChatRoomException(ChatErrorCode.NOT_EXIST_ROOM));
    }
}
