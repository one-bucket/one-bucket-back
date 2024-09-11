package com.onebucket.domain.WalletManage.service;

import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.dao.ProfileRepository;
import com.onebucket.domain.WalletManage.dao.WalletRepository;
import com.onebucket.domain.memberManage.domain.Profile;
import com.onebucket.domain.WalletManage.domain.Wallet;
import com.onebucket.domain.WalletManage.dto.internal.BalanceDto;
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
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void createInitWallet(Long id) {
        Profile profile = profileRepository.findProfileWithWalletById(id)
                .orElseThrow(() -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER_PROFILE));
        Wallet wallet = Wallet.create(profile);
        try {
            walletRepository.save(wallet);
        } catch (DataAccessException e) {
            throw new WalletManageException(WalletErrorCode.DATA_ACCESS_ERROR);
        } catch (Exception e) {
            throw new WalletManageException(WalletErrorCode.INTERNAL_ERROR);
        }
    }

    @Override
    @Transactional
    public BigDecimal addBalance(BalanceDto dto) {
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
    public BigDecimal deductBalance(BalanceDto dto) {
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

    @Override
    public BigDecimal getBalance(String username) {
        Long id = usernameToId(username);
        Wallet wallet = walletRepository.findById(id).orElseThrow(
                () -> new WalletManageException(WalletErrorCode.WALLET_NOT_FOUND));
        return wallet.getBalance();
    }

    // 공통 로직을 처리하는 메서드
    private Wallet findWalletAndValidate(BalanceDto dto) {
        validateAmount(dto);  // 금액 검증
        Long id = usernameToId(dto.username());
        Profile profile = profileRepository.findProfileWithWalletById(id)
                .orElseThrow(() -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER_PROFILE));
        return profile.getWallet();
    }

    // 금액 검증 로직
    private void validateAmount(BalanceDto dto) {
        BigDecimal amount = dto.amount();
        if(amount == null) {
            throw new WalletManageException(WalletErrorCode.AMOUNT_CANNOT_BE_NULL);
        }
        if(amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new WalletManageException(WalletErrorCode.AMOUNT_MUST_BE_POSITIVE);
        }
    }

    private Long usernameToId(String username) {
        return memberRepository.findIdByUsername(username)
                .orElseThrow(() -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));
    }
}
