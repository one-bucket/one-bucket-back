package com.onebucket.domain.tradeManage.service;

import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.tradeManage.dao.PendingTradeRepository;
import com.onebucket.domain.tradeManage.dto.internal.UserTradeDto;
import com.onebucket.domain.tradeManage.entity.PendingTrade;
import com.onebucket.global.exceptionManage.customException.TradeManageException.PendingTradeException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.TradeErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
@RequiredArgsConstructor
public class PendingTradeServiceImpl {

    private final PendingTradeRepository pendingTradeRepository;
    private final MemberRepository memberRepository;

    public void addMember(UserTradeDto dto) {
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
        pendingTradeRepository.save(pendingTrade);
    }
    public void quitMember(UserTradeDto dto) {
        Long userId = dto.getUserId();
        Long tradeId = dto.getTradeId();

        Member member = findMember(userId);
        PendingTrade pendingTrade = findPendingTrade(tradeId);

        pendingTrade.deleteMember(member);
        pendingTradeRepository.save(pendingTrade);
    }

    public boolean makeFinish(Long tradeId, boolean isFin) {
        PendingTrade pendingTrade = findPendingTrade(tradeId);

        pendingTrade.setFin(isFin);

        PendingTrade savedPendingTrade = pendingTradeRepository.save(pendingTrade);
        return savedPendingTrade.isFin();
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
