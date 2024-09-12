package com.onebucket.domain.walletManage.dao;

import com.onebucket.domain.walletManage.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <br>package name   : com.onebucket.domain.memberManage.dao
 * <br>file name      : WalletRepository
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
public interface WalletRepository extends JpaRepository<Wallet, Long> {
}
