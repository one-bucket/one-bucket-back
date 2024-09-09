package com.onebucket.domain.paymentManage.service;

import com.onebucket.domain.paymentManage.domain.Payment;
import com.onebucket.domain.paymentManage.dto.internal.CreatePaymentDto;
import com.onebucket.domain.paymentManage.dto.request.RequestCreatePaymentDto;
import com.onebucket.domain.paymentManage.dto.response.ResponsePaymentDto;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.paymentManage.service
 * <br>file name      : PaymentService
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
public interface PaymentService {
    Long createPayment(CreatePaymentDto dto);
    List<ResponsePaymentDto> getMemberPayment();
}
