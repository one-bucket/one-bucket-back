package com.onebucket.domain.tradeManage.service;

import com.onebucket.domain.chatManager.dao.ChatRoomMemberRepository;
import com.onebucket.domain.chatManager.dao.ChatRoomRepository;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.tradeManage.dao.TradeTagRepository;
import com.onebucket.domain.tradeManage.dao.pendingTrade.TradeRepository;
import com.onebucket.domain.tradeManage.dto.BaseTradeDto;
import com.onebucket.domain.tradeManage.dto.TradeKeyDto;
import com.onebucket.domain.tradeManage.entity.BaseTrade;
import com.onebucket.domain.tradeManage.entity.TradeTag;
import com.onebucket.global.exceptionManage.customException.TradeManageException.PendingTradeException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.TradeErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.service
 * <br>file name      : AbstractTradeServiceImpl
 * <br>date           : 11/13/24
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

@RequiredArgsConstructor
public abstract class AbstractTradeService<T extends BaseTrade, R extends TradeRepository<T>>
        implements TradeService {
    protected final R repository;
    protected final TradeTagRepository tradeTagRepository;
    protected final MemberRepository memberRepository;
    protected final ChatRoomRepository chatRoomRepository;
    protected final ChatRoomMemberRepository chatRoomMemberRepository;

    @Override
    @Transactional
    public <D extends BaseTradeDto.Create> Long createTrade(D dto) {
        Member member = findMember(dto.getOwnerId());
        TradeTag tag = findTag(dto.getTag());

        T trade = convertCreateTradeToBaseTrade(dto, member, tag);
        T savedTrade = repository.save(trade);

        return savedTrade.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public <D extends BaseTradeDto.Info> D getInfo(Long tradeId) {
        T trade = findTrade(tradeId);

        return convertTradeToInfoDto(trade);
    }

    @Override
    @Transactional
    public <D extends BaseTradeDto.Update> void update(D dto) {
        T trade = findTrade(dto.getTradeId());
        TradeTag tag = findTag(dto.getTag());

        updateTrade(dto, trade, tag);

        repository.save(trade);
    }

    @Override
    @Transactional
    public boolean makeFin(TradeKeyDto.Finish dto) {
        T trade = findTrade(dto.getTradeId());
        trade.setFin(dto.isFin());

        return repository.save(trade).isFin();
    }


    @Override
    @Transactional
    public LocalDateTime extendDueDate(TradeKeyDto.ExtendDate dto) {
        T trade = findTrade(dto.getTradeId());

        trade.extendDueDate(dto.getDate());
        trade.setUpdateAt(LocalDateTime.now());
        return repository.save(trade).getDueDate();
    }


    @Override
    @Transactional
    public Long getOwnerOfTrade(TradeKeyDto.FindTrade dto) {
        T trade = findTrade(dto.getTradeId());
        return trade.getOwner().getId();
    }


    protected Member findMember(Long userId) {
        return memberRepository.findById(userId).orElseThrow(() ->
                new AuthenticationException((AuthenticationErrorCode.NON_EXIST_AUTHENTICATION)));
    }

    protected TradeTag findTag(String tag) {
        return tradeTagRepository.findById(tag).orElseThrow(() ->
                new PendingTradeException(TradeErrorCode.UNKNOWN_TAG));
    }

    protected T findTrade(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new PendingTradeException(TradeErrorCode.UNKNOWN_TRADE));
    }


    protected abstract <D extends BaseTradeDto.Create> T convertCreateTradeToBaseTrade(D dto, Member owner, TradeTag tag);
    protected abstract <D extends BaseTradeDto.Info> D convertTradeToInfoDto(T trade);
    protected abstract <D extends BaseTradeDto.Update> void updateTrade(D dto, T trade, TradeTag tag);
    protected abstract Long makeTradeClosed(T trade);


}
