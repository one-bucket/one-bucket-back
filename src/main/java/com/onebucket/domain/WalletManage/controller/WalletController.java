package com.onebucket.domain.WalletManage.controller;

import com.onebucket.domain.WalletManage.dto.internal.AddBalanceDto;
import com.onebucket.domain.WalletManage.dto.internal.DeductBalanceDto;
import com.onebucket.domain.WalletManage.dto.request.RequestAddBalanceDto;
import com.onebucket.domain.WalletManage.dto.request.RequestDeductBalanceDto;
import com.onebucket.domain.WalletManage.dto.response.ResponseBalanceDto;
import com.onebucket.domain.WalletManage.service.WalletService;
import com.onebucket.global.utils.SecurityUtils;
import jakarta.validation.Valid;
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
    public ResponseEntity<ResponseBalanceDto> chargeMoney(@Valid @RequestBody RequestAddBalanceDto dto) {
        String username = securityUtils.getCurrentUsername();
        AddBalanceDto addBalanceDto = AddBalanceDto.builder().amount(dto.amount()).username(username).build();
        BigDecimal balance = walletService.addBalance(addBalanceDto);
        ResponseBalanceDto response = ResponseBalanceDto.of(balance);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deduct")
    public ResponseEntity<ResponseBalanceDto> deductMoney(@Valid @RequestBody RequestDeductBalanceDto dto) {
        String username = securityUtils.getCurrentUsername();
        DeductBalanceDto deductBalanceDto = DeductBalanceDto.builder().amount(dto.amount()).username(username).build();
        BigDecimal balance = walletService.addBalance(addBalanceDto);
        ResponseBalanceDto response = ResponseBalanceDto.of(balance);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ResponseBalanceDto> getMoney() {
        String username = securityUtils.getCurrentUsername();
        BigDecimal balance = walletService.getBalance(username);
        ResponseBalanceDto response = ResponseBalanceDto.of(balance);
        return ResponseEntity.ok(response);
    }
}
