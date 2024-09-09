package com.onebucket.domain.memberManage.service;

import com.onebucket.domain.memberManage.dao.ProfileRepository;
import com.onebucket.domain.memberManage.dao.WalletRepository;
import com.onebucket.domain.memberManage.domain.Profile;
import com.onebucket.domain.memberManage.domain.Wallet;
import com.onebucket.domain.memberManage.dto.request.RequestAddBalanceDto;
import com.onebucket.domain.memberManage.dto.request.RequestBalanceDto;
import com.onebucket.domain.memberManage.dto.request.RequestDeductBalanceDto;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.MemberManageException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.WalletErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <br>package name   : com.onebucket.domain.memberManage.service
 * <br>file name      : WalletServiceImpl
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
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final ProfileRepository profileRepository;

    @Override
    @Transactional
    public void addBalance(RequestAddBalanceDto dto) {
        validateAmount(dto);
        Profile profile = profileRepository.findProfileWithWalletById(dto.getProfileId()).orElseThrow(
                () -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER_PROFILE));
        Wallet wallet = profile.getWallet();
        wallet.addBalance(dto.amount());
    }

    /**
     * 중복되는 로직을 합칠 수 있는지 좀 더 고민해보자. dto 자체를 수정해야 될지도 모른다.
     * @param dto
     */
    @Override
    @Transactional
    public void deductBalance(RequestDeductBalanceDto dto) {
        validateAmount(dto);
        Profile profile = profileRepository.findProfileWithWalletById(dto.getProfileId()).orElseThrow(
                () -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER_PROFILE));
        Wallet wallet = profile.getWallet();
        wallet.deductBalance(dto.amount());
    }

    private void validateAmount(RequestBalanceDto dto) {
        BigDecimal amount = dto.amount();
        if(amount == null) {
            throw new MemberManageException(WalletErrorCode.AMOUNT_CANNOT_BE_NULL);
        }
        if(amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new MemberManageException(WalletErrorCode.AMOUNT_MUST_BE_POSITIVE);
        }
    }
}
