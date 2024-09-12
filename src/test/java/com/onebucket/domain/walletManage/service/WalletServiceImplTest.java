package com.onebucket.domain.walletManage.service;

import com.onebucket.domain.walletManage.dao.WalletRepository;
import com.onebucket.domain.walletManage.domain.Wallet;
import com.onebucket.domain.memberManage.dao.ProfileRepository;
import com.onebucket.domain.memberManage.domain.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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

    @InjectMocks
    private WalletServiceImpl walletService;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("Wallet 생성 성공")
    void createInitWallet() {
        Long id = 1L;
        doReturn(Optional.of(Profile.builder().build())).when(profileRepository).findProfileWithWalletById(id);
        walletService.createInitWallet(id);

        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    @DisplayName("잔액 충전하기 성공")
    void addBalance() {
    }

    @Test
    @DisplayName("잔액 소모하기 성공")
    void deductBalance() {
    }

    @Test
    @DisplayName("잔액 반환하기 성공")
    void getBalance() {
    }
}