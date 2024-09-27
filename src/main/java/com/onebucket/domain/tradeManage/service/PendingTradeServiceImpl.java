package com.onebucket.domain.tradeManage.service;

import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.tradeManage.dao.CloseTradeRepository;
import com.onebucket.domain.tradeManage.dao.PendingTradeRepository;
import com.onebucket.domain.tradeManage.dto.internal.UpdatePendingTradeDto;
import com.onebucket.domain.tradeManage.dto.internal.UserTradeDto;
import com.onebucket.domain.tradeManage.dto.request.TradeFinishDto;
import com.onebucket.domain.tradeManage.entity.CloseTrade;
import com.onebucket.domain.tradeManage.entity.PendingTrade;
import com.onebucket.global.exceptionManage.customException.TradeManageException.PendingTradeException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.TradeErrorCode;
import com.onebucket.global.utils.EntityUtils;
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
    private final CloseTradeRepository closeTradeRepository;
    private final MemberRepository memberRepository;


    @Override
    public Long addMember(UserTradeDto dto) {
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
        return pendingTradeRepository.save(pendingTrade).getId();
    }
    @Override
    public void quitMember(UserTradeDto dto) {
        Long userId = dto.getUserId();
        Long tradeId = dto.getTradeId();

        Member member = findMember(userId);
        PendingTrade pendingTrade = findPendingTrade(tradeId);
        if(!pendingTrade.getOwner().equals(member)) {
            throw new PendingTradeException(TradeErrorCode.NOT_OWNER_OF_TRADE);
        }
        pendingTrade.deleteMember(member);
        pendingTradeRepository.save(pendingTrade);
    }

    @Override
    public boolean makeFinish(TradeFinishDto.InternalTradeFinishDto dto) {
        Long tradeId = dto.getTradeId();
        boolean isFin = dto.isValue();
        PendingTrade pendingTrade = findPendingTrade(tradeId);

        pendingTrade.setFin(isFin);

        PendingTrade savedPendingTrade = pendingTradeRepository.save(pendingTrade);
        return savedPendingTrade.isFin();
    }


    @Override
    public void update(UpdatePendingTradeDto dto) {
        PendingTrade pendingTrade = findPendingTrade(dto.getId());

        EntityUtils.updateIfNotNull(dto.getItem(), pendingTrade::setItem);
        EntityUtils.updateIfNotNull(dto.getWanted(), pendingTrade::setWanted);
        EntityUtils.updateIfNotNull(dto.getJoins(), pendingTrade::setJoins);
        EntityUtils.updateIfNotNull(dto.getPrice(), pendingTrade::setPrice);
        EntityUtils.updateIfNotNull(dto.getCount(), pendingTrade::setCount);
        EntityUtils.updateIfNotNull(dto.getLocation(), pendingTrade::setLocation);
        pendingTradeRepository.save(pendingTrade);
    }

    @Override
    public List<String> getMembersNick(Long tradeId) {
        PendingTrade pendingTrade = findPendingTrade(tradeId);
        List<Member> members =  pendingTrade.getMembers();

        return members.stream().map(Member::getNickname).toList();
    }

    @Override
    public void deleteTrade(Long tradeId) {
        pendingTradeRepository.deleteById(tradeId);
    }
    @Override
    public Long closeTrade(Long tradeId) {
        PendingTrade pendingTrade = findPendingTrade(tradeId);
        List<Long> memberIds = pendingTrade.getMembers().stream().map(Member::getId).toList();

        CloseTrade closeTrade = CloseTrade.builder()
                .item(pendingTrade.getItem())
                .finishTradeAt(pendingTrade.getFinishTradeAt())
                .startTradeAt(pendingTrade.getStartTradeAt())
                .memberIds(memberIds)
                .build();
        return closeTradeRepository.save(closeTrade).getId();
    }

    @Override
    public LocalDateTime extendDueDate(Long tradeId) {
        PendingTrade pendingTrade = findPendingTrade(tradeId);

        pendingTrade.extendDueDate();

        return pendingTradeRepository.save(pendingTrade).getDueDate();
    }


    private Member findMember(Long userId) {
        return memberRepository.findById(userId).orElseThrow(() ->
                new AuthenticationException((AuthenticationErrorCode.NON_EXIST_AUTHENTICATION)));
    }


    private PendingTrade findPendingTrade(Long tradeId) {
        return pendingTradeRepository.findById(tradeId).orElseThrow(() ->
                new PendingTradeException(TradeErrorCode.UNKNOWN_TRADE));
    }
}
