package com.onebucket.domain.tradeManage.service;

import com.onebucket.domain.chatManager.dao.ChatRoomMemberRepository;
import com.onebucket.domain.chatManager.dao.ChatRoomRepository;
import com.onebucket.domain.chatManager.entity.ChatRoom;
import com.onebucket.domain.chatManager.entity.ChatRoomMemberId;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.tradeManage.dao.CloseTradeRepository;
import com.onebucket.domain.tradeManage.dao.PendingTradeRepository;
import com.onebucket.domain.tradeManage.dao.TradeTagRepository;
import com.onebucket.domain.tradeManage.dto.TradeDto;
import com.onebucket.domain.tradeManage.dto.TradeKeyDto;
import com.onebucket.domain.tradeManage.entity.CloseTrade;
import com.onebucket.domain.tradeManage.entity.PendingTrade;
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
public class PendingTradeServiceImpl implements PendingTradeService {

    private final PendingTradeRepository pendingTradeRepository;
    private final TradeTagRepository tradeTagRepository;
    private final CloseTradeRepository closeTradeRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Override
    public Long create(TradeDto.Create dto) {
        Member member = findMember(dto.getOwnerId());
        TradeTag tag = findTag(dto.getTag());

        PendingTrade pendingTrade = TradeDto.to(dto, member, tag);

        return pendingTradeRepository.save(pendingTrade).getId();
    }
    @Override
    public TradeDto.Info getInfo(Long tradeId) {

        PendingTrade pendingTrade = findPendingTrade(tradeId);

        return TradeDto.Info.of(pendingTrade);
    }
    @Override
    public void update(TradeDto.Update dto) {
        PendingTrade pendingTrade = findPendingTrade(dto.getId());
        TradeTag tag = findTag(dto.getTag());

        pendingTrade.setItem(dto.getItem());
        pendingTrade.setWanted(dto.getWanted());
        pendingTrade.setPrice(dto.getPrice());
        pendingTrade.setCount(dto.getCount());
        pendingTrade.setLocation(dto.getLocation());
        pendingTrade.setLinkUrl(dto.getLinkUrl());
        pendingTrade.setTradeTag(tag);

        pendingTradeRepository.save(pendingTrade);
    }
    @Override
    //새로운 유저가 해당 거래에 참여
    public TradeDto.ResponseJoinTrade addMember(TradeKeyDto.UserTrade dto) {
        Long userId = dto.getUserId();
        Long tradeId = dto.getTradeId();

        Member member = findMember(userId);
        PendingTrade pendingTrade = findPendingTrade(tradeId);

        List<Member> members = pendingTrade.getMembers();
        Long wanted = pendingTrade.getWanted();
        LocalDateTime dueDate = pendingTrade.getDueDate();
        if(members.size() >= wanted) {
            throw new PendingTradeException(TradeErrorCode.FULL_TRADE);
        }
        if(members.contains(member)) {
            throw new PendingTradeException(TradeErrorCode.ALREADY_JOIN);
        }
        if(pendingTrade.isFin()) {
            throw new PendingTradeException(TradeErrorCode.FINISH_TRADE);
        }
        if(dueDate.isBefore(LocalDateTime.now())) {
            throw new PendingTradeException(TradeErrorCode.DUE_DATE_OVER);
        }

        pendingTrade.addMember(member);

        //chatRoom에 멤버 추가
        ChatRoom chatRoom = pendingTrade.getChatRoom();
        chatRoom.addMember(member);

        Long savedTradeId = pendingTradeRepository.save(pendingTrade).getId();

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
        PendingTrade pendingTrade = findPendingTrade(tradeId);
        if(pendingTrade.getOwner().equals(member)) {
            throw new PendingTradeException(TradeErrorCode.OWNER_CANNOT_QUIT);
        }
        pendingTrade.deleteMember(member);

        //chatRoom에서 삭제
        String chatRoomId = pendingTrade.getChatRoom().getId();
        ChatRoomMemberId chatRoomMemberId = ChatRoomMemberId.builder()
                .member(userId)
                .chatRoom(chatRoomId)
                .build();
        chatRoomMemberRepository.deleteById(chatRoomMemberId);
        pendingTradeRepository.save(pendingTrade);
    }
    @Override
    public boolean makeFinish(TradeKeyDto.Finish dto) {
        Long tradeId = dto.getTradeId();
        boolean isFin = dto.isFin();
        PendingTrade pendingTrade = findPendingTrade(tradeId);

        pendingTrade.setFin(isFin);

        PendingTrade savedPendingTrade = pendingTradeRepository.save(pendingTrade);
        return savedPendingTrade.isFin();
    }

    //TODO: 종료 프로토콜 좀 더 다듬어봐야 할듯
    @Override
    public Long terminate(TradeKeyDto.FindTrade dto) {
        PendingTrade pendingTrade = findPendingTrade(dto.getTradeId());

        List<Long> memberIds = pendingTrade.getMembers().stream().map(Member::getId).toList();

        CloseTrade closeTrade = CloseTrade.builder()
                .item(pendingTrade.getItem())
                .finishTradeAt(pendingTrade.getFinishTradeAt())
                .startTradeAt(pendingTrade.getStartTradeAt())
                .memberIds(memberIds)
                .build();

        pendingTradeRepository.delete(pendingTrade);
        return closeTradeRepository.save(closeTrade).getId();
    }
    @Override
    public LocalDateTime extendDueDate(TradeKeyDto.ExtendDate dto) {
        PendingTrade pendingTrade = findPendingTrade(dto.getTradeId());

        pendingTrade.extendDueDate(dto.getDate());

        return pendingTradeRepository.save(pendingTrade).getDueDate();
    }

    @Override
    public void setChatRoom(TradeKeyDto.SettingChatRoom dto) {
        PendingTrade pendingTrade = findPendingTrade(dto.getTradeId());
        ChatRoom chatRoom = findChatRoom(dto.getChatRoomId());

        pendingTrade.setChatRoom(chatRoom);

        pendingTradeRepository.save(pendingTrade);
    }


    private Member findMember(Long userId) {
        return memberRepository.findById(userId).orElseThrow(() ->
                new AuthenticationException((AuthenticationErrorCode.NON_EXIST_AUTHENTICATION)));
    }


    private PendingTrade findPendingTrade(Long tradeId) {
        return pendingTradeRepository.findById(tradeId).orElseThrow(() ->
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
