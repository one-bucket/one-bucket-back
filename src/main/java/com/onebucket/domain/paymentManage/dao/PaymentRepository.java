package com.onebucket.domain.paymentManage.dao;

import com.onebucket.domain.paymentManage.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <br>package name   : com.onebucket.domain.paymentManage.dao
 * <br>file name      : PaymentRepository
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
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByMember(Long memberId);
}
