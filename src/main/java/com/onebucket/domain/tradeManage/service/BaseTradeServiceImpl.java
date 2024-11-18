package com.onebucket.domain.tradeManage.service;

import com.onebucket.domain.chatManager.dao.ChatRoomMemberRepository;
import com.onebucket.domain.chatManager.dao.ChatRoomRepository;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.tradeManage.dao.TradeTagRepository;
import com.onebucket.domain.tradeManage.dao.pendingTrade.BaseTradeRepository;
import com.onebucket.domain.tradeManage.dto.BaseTradeDto;
import com.onebucket.domain.tradeManage.entity.BaseTrade;
import com.onebucket.domain.tradeManage.entity.TradeTag;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.service
 * <br>file name      : BaseTradeServiceImpl
 * <br>date           : 11/15/24
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
public class BaseTradeServiceImpl extends AbstractTradeService<BaseTrade, BaseTradeRepository> {


    public BaseTradeServiceImpl(BaseTradeRepository repository,
                                TradeTagRepository tradeTagRepository,
                                MemberRepository memberRepository,
                                ChatRoomRepository chatRoomRepository,
                                ChatRoomMemberRepository chatRoomMemberRepository) {
        super(repository, tradeTagRepository, memberRepository, chatRoomRepository, chatRoomMemberRepository);
    }

    @Override
    protected <D extends BaseTradeDto.Create> BaseTrade convertCreateTradeToBaseTrade(D dto, Member owner, TradeTag tag) {
        BaseTrade baseTrade = makeCreateDtoToBaseTrade(dto);
        baseTrade.setOwner(owner);
        baseTrade.setTradeTag(tag);

        return baseTrade;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected BaseTradeDto.Info convertTradeToInfoDto(BaseTrade trade) {
        return makeBaseTradeToDto(trade);
    }

    @Override
    protected <D extends BaseTradeDto.UpdateTrade> void updateTrade(D dto) {

        BaseTrade baseTrade = dto.getTrade();

        baseTrade.setPrice(dto.getPrice());
        baseTrade.setItem(dto.getItem());
        baseTrade.setLocation(dto.getLocation());
        baseTrade.setLinkUrl(dto.getLinkUrl());
        baseTrade.setTradeTag(dto.getTag());
        baseTrade.setUpdateAt(LocalDateTime.now());
    }

    @Override
    protected Long makeTradeClosed(BaseTrade trade) {
        trade.setFin(true);
        return -1L;
    }

    private BaseTrade makeCreateDtoToBaseTrade(BaseTradeDto.Create dto) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTimeDueDate = now.plusDays(dto.getDueDate());

        return BaseTrade.builder()
                .item(dto.getItem())
                .linkUrl(dto.getLinkUrl())
                .price(dto.getPrice())
                .dueDate(localDateTimeDueDate)
                .location(dto.getLocation())
                .isFin(false)
                .createAt(now)
                .build();
    }

    private BaseTradeDto.Info makeBaseTradeToDto(BaseTrade baseTrade) {
        return BaseTradeDto.Info.builder()
                .id(baseTrade.getId())
                .userId(baseTrade.getOwner().getId())
                .tag(baseTrade.getTradeTag().getName())
                .item(baseTrade.getItem())
                .linkUrl(baseTrade.getLinkUrl())
                .price(baseTrade.getPrice())
                .dueDate(baseTrade.getDueDate())
                .location(baseTrade.getLocation())
                .isFin(baseTrade.isFin())
                .createAt(baseTrade.getCreateAt())
                .updateAt(baseTrade.getUpdateAt())
                .build();
    }
}
