package com.onebucket.domain.walletManage.service;

import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.walletManage.dao.WalletRepository;
import com.onebucket.domain.walletManage.domain.Wallet;
import com.onebucket.domain.memberManage.dao.ProfileRepository;
import com.onebucket.domain.memberManage.domain.Profile;
import com.onebucket.domain.walletManage.dto.internal.AddBalanceDto;
import com.onebucket.domain.walletManage.dto.internal.DeductBalanceDto;
import com.onebucket.domain.walletManage.dto.request.RequestAddBalanceDto;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.WalletManageException;
import com.onebucket.global.exceptionManage.errorCode.WalletErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * <br>package name   : com.onebucket.domain.WalletManage.service
 * <br>file name      : WalletServiceImplTest
 * <br>date           : 2024-09-12
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
 * 2024-09-12        SeungHoon              init create
 * </pre>
 */
@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Wallet 생성 성공")
    void createInitWallet_success() {
        Long id = -1L;
        doReturn(Optional.of(Profile.builder().build())).when(profileRepository).findProfileWithWalletById(id);
        walletService.createInitWallet(id);

        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    @DisplayName("Wallet 생성 실패 - profile이 없음")
    void createInitWallet_fail_profileNotFound() {
        Long id = -1L;
        when(profileRepository.findProfileWithWalletById(id)).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class,
                () -> walletService.createInitWallet(id));
    }

    @Test
    @DisplayName("Wallet 생성 실패 - 저장 과정에서 문제가 생김")
    void createInitWallet_fail_problemFromSave() {
        Long id = -1L;
        doReturn(Optional.of(Profile.builder().build())).when(profileRepository).findProfileWithWalletById(id);
        doThrow(new WalletManageException(WalletErrorCode.DATA_ACCESS_ERROR)).when(walletRepository).save(any(Wallet.class));

        assertThrows(WalletManageException.class,
                () -> walletService.createInitWallet(id));
    }

    @Test
    @DisplayName("Wallet 금액 충전하기 성공")
    void addBalance_success() {
        String username = "user1";
        BigDecimal amountToAdd = BigDecimal.valueOf(10000);
        AddBalanceDto addBalanceDto = AddBalanceDto.builder()
                .amount(BigDecimal.valueOf(5000))
                .username(username)
                .build();
        Profile profile = Profile.builder().id(-1L).build();
        Wallet wallet = Wallet.create(profile);
        // Profile에 Wallet 설정
        profile.setWallet(wallet);
        wallet.addBalance(amountToAdd);

        when(memberRepository.findIdByUsername(username)).thenReturn(Optional.of(1L));
        when(profileRepository.findProfileWithWalletById(1L)).thenReturn(Optional.of(profile));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        BigDecimal updatedBalance = walletService.addBalance(addBalanceDto);
        assertEquals(updatedBalance, BigDecimal.valueOf(15000));
    }

    @Test
    @DisplayName("Wallet 금액 충전 실패 - 잔액 값이 null임")
    void addBalance_fail_amountIsNull() {
        String username = "user1";
        AddBalanceDto addBalanceDto = AddBalanceDto.builder()
        .amount(null)
        .username(username)
        .build();

        // When & Then
        assertThatThrownBy(()->walletService.addBalance(addBalanceDto))
                .isInstanceOf(WalletManageException.class)
                    .extracting("errorCode")
                        .isEqualTo(WalletErrorCode.AMOUNT_CANNOT_BE_NULL);

        // 금액 검증 로직에서 예외가 발생하므로, walletRepository는 호출되지 않아야 함
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    @DisplayName("Wallet 금액 충전 실패 - amount가 음수임")
    void addBalance_fail_amountIsNegative() {
        String username = "user1";
        AddBalanceDto addBalanceDto = AddBalanceDto.builder()
                .amount(BigDecimal.valueOf(-10000))
                .username(username)
                .build();

        // When & Then
        assertThatThrownBy(()->walletService.addBalance(addBalanceDto))
                .isInstanceOf(WalletManageException.class)
                .extracting("errorCode")
                .isEqualTo(WalletErrorCode.AMOUNT_MUST_BE_POSITIVE);


        // 금액 검증 로직에서 예외가 발생하므로, walletRepository는 호출되지 않아야 함
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    @DisplayName("Wallet 잔액 소모하기 성공")
    void deductBalance_success() {
        String username = "user1";
        BigDecimal amountToAdd = BigDecimal.valueOf(10000);
        DeductBalanceDto deductBalanceDto = DeductBalanceDto.builder()
                .amount(BigDecimal.valueOf(5000))
                .username(username)
                .build();
        Profile profile = Profile.builder().id(-1L).build();
        Wallet wallet = Wallet.create(profile);
        // Profile에 Wallet 설정
        profile.setWallet(wallet);
        wallet.addBalance(amountToAdd);

        when(memberRepository.findIdByUsername(username)).thenReturn(Optional.of(1L));
        when(profileRepository.findProfileWithWalletById(1L)).thenReturn(Optional.of(profile));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        BigDecimal updatedBalance = walletService.deductBalance(deductBalanceDto);
        assertEquals(updatedBalance, BigDecimal.valueOf(5000));

    }

    @Test
    @DisplayName("Wallet 잔액 소모하기 실패 - 잔액이 적어 결제할 수 없음")
    void deductBalance_fail_notSufficientToPay() {
        String username = "user1";
        BigDecimal amountToAdd = BigDecimal.valueOf(0);
        DeductBalanceDto deductBalanceDto = DeductBalanceDto.builder()
                .amount(BigDecimal.valueOf(5000))
                .username(username)
                .build();
        Profile profile = Profile.builder().id(-1L).build();
        Wallet wallet = Wallet.create(profile);
        // Profile에 Wallet 설정
        profile.setWallet(wallet);
        wallet.addBalance(amountToAdd);

        when(memberRepository.findIdByUsername(username)).thenReturn(Optional.of(1L));
        when(profileRepository.findProfileWithWalletById(1L)).thenReturn(Optional.of(profile));

        assertThatThrownBy(()->walletService.deductBalance(deductBalanceDto))
                .isInstanceOf(WalletManageException.class)
                .extracting("errorCode")
                .isEqualTo(WalletErrorCode.INSUFFICIENT_BALANCE);
    }

    @Test
    @DisplayName("Wallet 잔액 반환하기 성공")
    void getBalance_success() {
        String username = "user1";
        Long id = -1L;
        Profile profile = Profile.builder().id(id).build();
        Wallet wallet = Wallet.create(profile);
        // Profile에 Wallet 설정
        profile.setWallet(wallet);

        when(memberRepository.findIdByUsername(username)).thenReturn(Optional.of(id));
        when(walletRepository.findById(id)).thenReturn(Optional.of(wallet));

        BigDecimal balance = walletService.getBalance(username);
        assertEquals(BigDecimal.ZERO,balance);
    }

    @Test
    @DisplayName("Wallet 잔액 반환하기 실패 - 존재하지 않은 Wallet")
    void getBalance_fail_WalletNotFound() {
        String username = "user1";
        Long id = -1L;
        when(memberRepository.findIdByUsername(username)).thenReturn(Optional.of(id));

        assertThatThrownBy(()->walletService.getBalance(username))
                .isInstanceOf(WalletManageException.class)
                .extracting("errorCode")
                .isEqualTo(WalletErrorCode.WALLET_NOT_FOUND);
    }
}