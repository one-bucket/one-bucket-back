package com.onebucket.domain.tradeManage.service;

import com.onebucket.domain.chatManager.dao.ChatRoomMemberRepository;
import com.onebucket.domain.chatManager.dao.ChatRoomRepository;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.tradeManage.dao.TradeTagRepository;
import com.onebucket.domain.tradeManage.dao.closedTrade.ClosedUsedTradeRepository;
import com.onebucket.domain.tradeManage.dao.pendingTrade.UsedTradeRepository;
import com.onebucket.domain.tradeManage.dto.BaseTradeDto;
import com.onebucket.domain.tradeManage.dto.TradeKeyDto;
import com.onebucket.domain.tradeManage.dto.UsedTradeDto;
import com.onebucket.domain.tradeManage.entity.ClosedUsedTrade;
import com.onebucket.domain.tradeManage.entity.TradeTag;
import com.onebucket.domain.tradeManage.entity.UsedTrade;
import com.onebucket.global.exceptionManage.customException.TradeManageException.TradeException;
import com.onebucket.global.exceptionManage.errorCode.TradeErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.service
 * <br>file name      : UsedTradeService
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
public class UsedTradeServiceImpl extends AbstractTradeService<UsedTrade, UsedTradeRepository>
        implements UsedTradeService {

    private final ClosedUsedTradeRepository closedUsedTradeRepository;

    public UsedTradeServiceImpl(UsedTradeRepository repository,
                                TradeTagRepository tradeTagRepository,
                                MemberRepository memberRepository,
                                ChatRoomRepository chatRoomRepository,
                                ChatRoomMemberRepository chatRoomMemberRepository,
                                ClosedUsedTradeRepository closedUsedTradeRepository) {
        super(repository, tradeTagRepository, memberRepository, chatRoomRepository, chatRoomMemberRepository);

        this.closedUsedTradeRepository = closedUsedTradeRepository;
    }

    @Override
    protected <D extends BaseTradeDto.Create> UsedTrade convertCreateTradeToBaseTrade(D dto, Member owner, TradeTag tag) {
        UsedTrade usedTrade = makeCreateDtoToUsedTrade((UsedTradeDto.Create) dto);
        usedTrade.setOwner(owner);
        usedTrade.setTradeTag(tag);

        return usedTrade;
    }

    @Override
    protected BaseTradeDto.Info convertTradeToInfoDto(UsedTrade trade) {
        return makeUsedTradeToDto(trade);
    }

    @Override
    protected <D extends BaseTradeDto.UpdateTrade> void updateTrade(D dto) {
        UsedTrade usedTrade = (UsedTrade) dto.getTrade();
        UsedTradeDto.UpdateTrade update = (UsedTradeDto.UpdateTrade) dto;


        usedTrade.setItem(update.getItem());
        usedTrade.setPrice(update.getPrice());
        usedTrade.setLocation(update.getLocation());
        usedTrade.setLinkUrl(update.getLinkUrl());
        usedTrade.setTradeTag(update.getTag());
        usedTrade.setUpdateAt(LocalDateTime.now());
    }

    @Override
    protected Long makeTradeClosed(UsedTrade trade) {
        Long id = closedUsedTradeRepository.save((ClosedUsedTrade) trade).getId();

        repository.delete(trade);
        return id;
    }

    @Transactional
    @Override
    public void reserve(TradeKeyDto.UserTrade dto) {
        Long tradeId = dto.getTradeId();
        UsedTrade usedTrade = findTrade(tradeId);
        if(usedTrade.getJoiner() != null) {
            throw new TradeException(TradeErrorCode.ALREADY_JOIN);
        }

        Member member = findMember(dto.getUserId());
        usedTrade.setJoiner(member);
        repository.save(usedTrade);
    }
    @Transactional
    @Override
    public void rejectReserve(TradeKeyDto.FindTrade dto) {
        UsedTrade usedTrade = findTrade(dto.getTradeId());
        usedTrade.setJoiner(null);

        repository.save(usedTrade);
    }



    private UsedTrade makeCreateDtoToUsedTrade(UsedTradeDto.Create dto) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTimeDueDate = now.plusDays(dto.getDueDate());

        return UsedTrade.builder()
                .item(dto.getItem())
                .linkUrl(dto.getLinkUrl())
                .price(dto.getPrice())
                .dueDate(localDateTimeDueDate)
                .location(dto.getLocation())
                .isFin(false)
                .createAt(now)
                .build();
    }

    private UsedTradeDto.Info makeUsedTradeToDto(UsedTrade usedTrade) {
        boolean isReserved = false;
        if(usedTrade.getJoiner() != null) {
            isReserved = true;
        }

        return UsedTradeDto.Info.builder()
                .id(usedTrade.getId())
                .userId(usedTrade.getOwner().getId())
                .tag(usedTrade.getTradeTag().getName())
                .item(usedTrade.getItem())
                .linkUrl(usedTrade.getLinkUrl())
                .price(usedTrade.getPrice())
                .dueDate(usedTrade.getDueDate())
                .location(usedTrade.getLocation())
                .isFin(usedTrade.isFin())
                .createAt(usedTrade.getCreateAt())
                .updateAt(usedTrade.getUpdateAt())

                .isReserve(isReserved)
                .build();
    }
}
