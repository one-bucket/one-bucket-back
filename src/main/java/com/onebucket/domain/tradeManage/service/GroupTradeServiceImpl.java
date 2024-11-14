package com.onebucket.domain.tradeManage.service;

import com.onebucket.domain.chatManager.dao.ChatRoomMemberRepository;
import com.onebucket.domain.chatManager.dao.ChatRoomRepository;
import com.onebucket.domain.chatManager.entity.ChatRoom;
import com.onebucket.domain.chatManager.entity.ChatRoomMemberId;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.tradeManage.dao.TradeTagRepository;
import com.onebucket.domain.tradeManage.dao.closedTrade.ClosedGroupTradeRepository;
import com.onebucket.domain.tradeManage.dao.pendingTrade.GroupTradeRepository;
import com.onebucket.domain.tradeManage.dto.BaseTradeDto;
import com.onebucket.domain.tradeManage.dto.GroupTradeDto;
import com.onebucket.domain.tradeManage.dto.TradeKeyDto;
import com.onebucket.domain.tradeManage.entity.ClosedGroupTrade;
import com.onebucket.domain.tradeManage.entity.GroupTrade;
import com.onebucket.domain.tradeManage.entity.TradeTag;
import com.onebucket.global.exceptionManage.customException.TradeManageException.PendingTradeException;
import com.onebucket.global.exceptionManage.customException.chatManageException.Exceptions.ChatRoomException;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import com.onebucket.global.exceptionManage.errorCode.TradeErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.service
 * <br>file name      : GroupTradeServiceImpl
 * <br>date           : 2024-11-14
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
public class GroupTradeServiceImpl extends AbstractTradeService<GroupTrade, GroupTradeRepository>
        implements GroupTradeService {

    private final ClosedGroupTradeRepository closedGroupTradeRepository;

    public GroupTradeServiceImpl(GroupTradeRepository repository,
                                 TradeTagRepository tradeTagRepository,
                                 MemberRepository memberRepository,
                                 ChatRoomRepository chatRoomRepository,
                                 ChatRoomMemberRepository chatRoomMemberRepository,
                                 ClosedGroupTradeRepository closedGroupTradeRepository, GroupTradeRepository groupTradeRepository) {
        super(repository, tradeTagRepository, memberRepository, chatRoomRepository, chatRoomMemberRepository);

        this.closedGroupTradeRepository = closedGroupTradeRepository;
    }

    @Override
    protected <D extends BaseTradeDto.Create> GroupTrade convertCreateTradeToBaseTrade(D dto, Member owner, TradeTag tag) {
        GroupTrade groupTrade = makeCreateDtoToGroupTrade((GroupTradeDto.Create) dto);
        groupTrade.setOwner(owner);
        groupTrade.setTradeTag(tag);

        return groupTrade;
    }

    @Override
    protected BaseTradeDto.Info convertTradeToInfoDto(GroupTrade trade) {
        return makeGroupTradeToDto(trade);
    }

    @Override
    protected <D extends BaseTradeDto.UpdateTrade> void updateTrade(D dto) {
        GroupTrade groupTrade = (GroupTrade) dto.getTrade();
        GroupTradeDto.UpdateTrade update = (GroupTradeDto.UpdateTrade) dto;


        groupTrade.setItem(update.getItem());
        groupTrade.setPrice(update.getPrice());
        groupTrade.setLocation(update.getLocation());
        groupTrade.setLinkUrl(update.getLinkUrl());
        groupTrade.setTradeTag(update.getTag());

        groupTrade.setWanted(update.getWanted());
        groupTrade.setCount(update.getCount());
    }

    @Override
    protected Long makeTradeClosed(GroupTrade trade) {
        Long id = closedGroupTradeRepository.save((ClosedGroupTrade) trade).getId();

        repository.delete(trade);

        return id;
    }

    @Override

    public TradeKeyDto.ResponseJoinTrade addMember(TradeKeyDto.UserTrade dto) {
        Long userId = dto.getUserId();
        Long tradeId = dto.getTradeId();

        Member member = findMember(userId);
        GroupTrade groupTrade = findTrade(tradeId);

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
        ChatRoom chatRoom = groupTrade.getChatRoom();
        if(chatRoom == null) {
            throw new ChatRoomException(ChatErrorCode.NOT_EXIST_ROOM);
        }
        chatRoom.addMember(member);
        repository.save(groupTrade);

        return TradeKeyDto.ResponseJoinTrade.builder()
                .tradeId(tradeId)
                .chatRoomId(chatRoom.getId())
                .build();
    }

    @Override
    @Transactional
    public void quitMember(TradeKeyDto.UserTrade dto) {
        Long userId = dto.getUserId();
        Long tradeId = dto.getTradeId();

        Member member = findMember(userId);
        GroupTrade groupTrade = findTrade(tradeId);
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
        repository.save(groupTrade);
    }

    @Override
    public void setChatRoom(TradeKeyDto.SettingChatRoom dto) {
        GroupTrade groupTrade = findTrade(dto.getTradeId());
        ChatRoom chatRoom = findChatRoom(dto.getChatRoomId());

        groupTrade.setChatRoom(chatRoom);
        repository.save(groupTrade);
    }

    private GroupTrade makeCreateDtoToGroupTrade(GroupTradeDto.Create dto) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTimeDueDate = now.plusDays(dto.getDueDate());
        return GroupTrade.builder()
                .item(dto.getItem())
                .linkUrl(dto.getLinkUrl())
                .price(dto.getPrice())
                .dueDate(localDateTimeDueDate)
                .location(dto.getLocation())
                .isFin(false)
                .createAt(now)

                .wanted(dto.getWanted())
                .joins(0L)
                .count(dto.getCount())

                .build();
    }

    private GroupTradeDto.Info makeGroupTradeToDto(GroupTrade groupTrade) {
        List<BaseTradeDto.Info.JoinMember> joinMembers = groupTrade.getJoiners().stream().map((member) ->
                BaseTradeDto.Info.JoinMember.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .imageUrl(member.getProfile().getImageUrl())
                        .build())
                .toList();

        return GroupTradeDto.Info.builder()
                .id(groupTrade.getId())
                .userId(groupTrade.getOwner().getId())
                .tag(groupTrade.getTradeTag().getName())
                .item(groupTrade.getItem())
                .linkUrl(groupTrade.getLinkUrl())
                .price(groupTrade.getPrice())
                .dueDate(groupTrade.getDueDate())
                .location(groupTrade.getLocation())
                .isFin(groupTrade.isFin())
                .createAt(groupTrade.getCreateAt())
                .updateAt(groupTrade.getUpdateAt())

                .wanted(groupTrade.getWanted())
                .joins(groupTrade.getJoins())
                .count(groupTrade.getCount())
                .joinMember(joinMembers)
                .build();
    }

    private ChatRoom findChatRoom(String chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElseThrow(() ->
                new ChatRoomException(ChatErrorCode.NOT_EXIST_ROOM));
    }
}
