package com.onebucket.domain.memberManage.service;

import com.onebucket.domain.memberManage.dao.ProfileRepository;
import com.onebucket.domain.memberManage.dao.WalletRepository;
import com.onebucket.domain.memberManage.domain.Profile;
import com.onebucket.domain.memberManage.domain.Wallet;
import com.onebucket.domain.memberManage.dto.request.RequestBalanceDto;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.WalletManageException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.WalletErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
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
    public BigDecimal addBalance(RequestBalanceDto dto) {
        Wallet wallet = findWalletAndValidate(dto);
        wallet.addBalance(dto.amount()); // 잔액 추가
        try {
            walletRepository.save(wallet);
            return wallet.getBalance();
        } catch (DataAccessException e) {
            throw new WalletManageException(WalletErrorCode.DATA_ACCESS_ERROR);
        } catch (Exception e) {
            throw new WalletManageException(WalletErrorCode.INTERNAL_ERROR);
        }
    }

    @Override
    @Transactional
    public BigDecimal deductBalance(RequestBalanceDto dto) {
        Wallet wallet = findWalletAndValidate(dto);
        wallet.deductBalance(dto.amount()); // 잔액 추가
        try {
            walletRepository.save(wallet);
            return wallet.getBalance();
        } catch (DataAccessException e) {
            throw new WalletManageException(WalletErrorCode.DATA_ACCESS_ERROR);
        } catch (Exception e) {
            throw new WalletManageException(WalletErrorCode.INTERNAL_ERROR);
        }
    }

    // 공통 로직을 처리하는 메서드
    private Wallet findWalletAndValidate(RequestBalanceDto dto) {
        validateAmount(dto);  // 금액 검증
        Profile profile = profileRepository.findProfileWithWalletById(dto.profileId())
                .orElseThrow(() -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER_PROFILE));
        return profile.getWallet();
    }

    // 금액 검증 로직
    private void validateAmount(RequestBalanceDto dto) {
        BigDecimal amount = dto.amount();
        if(amount == null) {
            throw new WalletManageException(WalletErrorCode.AMOUNT_CANNOT_BE_NULL);
        }
        if(amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new WalletManageException(WalletErrorCode.AMOUNT_MUST_BE_POSITIVE);
        }
    }

}
