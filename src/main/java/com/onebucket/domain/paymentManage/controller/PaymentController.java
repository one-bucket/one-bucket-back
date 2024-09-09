package com.onebucket.domain.paymentManage.controller;

import com.onebucket.domain.paymentManage.dto.internal.CreatePaymentDto;
import com.onebucket.domain.paymentManage.dto.request.RequestCreatePaymentDto;
import com.onebucket.domain.paymentManage.dto.response.ResponsePaymentDto;
import com.onebucket.domain.paymentManage.service.PaymentService;
import com.onebucket.global.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <br>package name   : com.onebucket.domain.paymentManage.controller
 * <br>file name      : PaymentController
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
@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final SecurityUtils securityUtils;

    @PostMapping("/payment")
    public ResponseEntity<Long> createPayment(@RequestBody RequestCreatePaymentDto request) {
        String username = securityUtils.getCurrentUsername();
        CreatePaymentDto dto = CreatePaymentDto.of(username,request);
        Long id = paymentService.createPayment(dto);
        return ResponseEntity.ok(id);
    }
}
