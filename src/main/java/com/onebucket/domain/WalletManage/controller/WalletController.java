package com.onebucket.domain.WalletManage.controller;

import com.onebucket.domain.WalletManage.dto.AddBalanceDto;
import com.onebucket.domain.WalletManage.dto.DeductBalanceDto;
import com.onebucket.domain.WalletManage.dto.request.RequestAddBalanceDto;
import com.onebucket.domain.WalletManage.dto.request.RequestDeductBalanceDto;
import com.onebucket.domain.WalletManage.service.WalletService;
import com.onebucket.global.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final SecurityUtils securityUtils;

    @PostMapping("/charge")
    public ResponseEntity<BigDecimal> chargeMoney(@RequestBody RequestAddBalanceDto dto) {
        String username = securityUtils.getCurrentUsername();
        AddBalanceDto addBalanceDto = AddBalanceDto.builder().amount(dto.amount()).username(username).build();
        BigDecimal response = walletService.addBalance(addBalanceDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deduct")
    public ResponseEntity<BigDecimal> deductMoney(@RequestBody RequestDeductBalanceDto dto) {
        String username = securityUtils.getCurrentUsername();
        DeductBalanceDto deductBalanceDto = DeductBalanceDto.builder().amount(dto.amount()).username(username).build();
        BigDecimal response = walletService.deductBalance(deductBalanceDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<BigDecimal> getMoney() {
        String username = securityUtils.getCurrentUsername();
        BigDecimal response = walletService.getBalance(username);
        return ResponseEntity.ok(response);
    }
}
