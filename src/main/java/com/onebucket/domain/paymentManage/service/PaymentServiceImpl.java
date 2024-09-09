package com.onebucket.domain.paymentManage.service;

import com.onebucket.domain.boardManage.dao.MarketPostRepository;
import com.onebucket.domain.boardManage.entity.post.MarketPost;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.paymentManage.dao.PaymentRepository;
import com.onebucket.domain.paymentManage.domain.Payment;
import com.onebucket.domain.paymentManage.dto.internal.CreatePaymentDto;
import com.onebucket.domain.paymentManage.dto.response.ResponsePaymentDto;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.MemberManageException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.error.Mark;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.paymentManage.service
 * <br>file name      : PaymentServiceImpl
 * <br>date           : 2024-09-09
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 * <pre>
 * modified log :
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-09-09        SeungHoon              init create
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final MarketPostRepository marketPostRepository;

    @Override
    @Transactional
    public Long createPayment(CreatePaymentDto dto) {
        Member member = memberRepository.findByUsername(dto.username())
                .orElseThrow(() -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));
        MarketPost marketPost = marketPostRepository.findById(dto.postId())
                .orElseThrow(() -> new RuntimeException("no MarketPost found"));

        Payment payment = Payment.create(member,marketPost,dto);
        paymentRepository.save(payment);
        return payment.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponsePaymentDto> getMemberPayment(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));
        Long buyerId = member.getId();
        List<Payment> paymentList = paymentRepository.findByBuyerId(buyerId);
        return paymentList.stream()
                .map(payment -> {
                    MarketPost marketPost = payment.getMarketPost();
                    return ResponsePaymentDto.of(marketPost,payment);
                })
                .toList();
    }
}
