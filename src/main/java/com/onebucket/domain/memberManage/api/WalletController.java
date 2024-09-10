package com.onebucket.domain.memberManage.api;

import com.onebucket.domain.memberManage.dto.request.RequestAddBalanceDto;
import com.onebucket.domain.memberManage.dto.request.RequestDeductBalanceDto;
import com.onebucket.domain.memberManage.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * <br>package name   : com.onebucket.domain.memberManage.api
 * <br>file name      : WalletController
 * <br>date           : 2024-09-10
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
 * 2024-09-10        SeungHoon              init create
 * </pre>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/wallet")
public class WalletController {
    private final WalletService walletService;

    @PostMapping("/charge")
    public ResponseEntity<BigDecimal> charge(@RequestBody RequestAddBalanceDto dto) {
        BigDecimal response = walletService.addBalance(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deduct")
    public ResponseEntity<BigDecimal> deduct(@RequestBody RequestDeductBalanceDto dto) {
        BigDecimal response = walletService.deductBalance(dto);
        return ResponseEntity.ok(response);
    }
}
