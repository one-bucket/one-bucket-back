package com.onebucket.domain.walletManage.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onebucket.domain.walletManage.dto.internal.AddBalanceDto;
import com.onebucket.domain.walletManage.dto.internal.DeductBalanceDto;
import com.onebucket.domain.walletManage.dto.request.RequestAddBalanceDto;
import com.onebucket.domain.walletManage.dto.request.RequestDeductBalanceDto;
import com.onebucket.domain.walletManage.service.WalletService;
import com.onebucket.global.exceptionManage.exceptionHandler.BaseExceptionHandler;
import com.onebucket.global.utils.SecurityUtils;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <br>package name   : com.onebucket.domain.walletManage.controller
 * <br>file name      : WalletControllerTest
 * <br>date           : 2024-09-14
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
 * 2024-09-14        SeungHoon              init create
 * </pre>
 */
@ExtendWith(MockitoExtension.class)
class WalletControllerTest {

    @Mock
    private WalletService walletService;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private WalletController walletController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(walletController)
                .setControllerAdvice(new BaseExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Wallet 잔액 충전하기 성공")
    void chargeMoney_success() throws Exception {
        final String username = "user1";
        final String url = "/wallet/charge";
        RequestAddBalanceDto requestAddBalanceDto = new RequestAddBalanceDto(BigDecimal.valueOf(10000));
        doReturn(username).when(securityUtils).getCurrentUsername();
        doReturn(BigDecimal.valueOf(10000)).when(walletService).addBalance(ArgumentMatchers.any(AddBalanceDto.class));

        mockMvc.perform(post(url)
                .content(objectMapper.writeValueAsString(requestAddBalanceDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(10000));
    }

    @Test
    @DisplayName("Wallet 잔액 충전하기 실패 - authentication 실패")
    void chargeMoney_fail_notExistAuth() throws Exception {

    }

    @Test
    @DisplayName("Wallet 금액 사용하기 성공")
    void deductMoney_success() throws Exception {
        final String username = "user1";
        final String url = "/wallet/deduct";
        RequestDeductBalanceDto requestDeductBalanceDto = new RequestDeductBalanceDto(BigDecimal.valueOf(10000));
        doReturn(username).when(securityUtils).getCurrentUsername();
        doReturn(BigDecimal.valueOf(10000)).when(walletService).deductBalance(ArgumentMatchers.any(DeductBalanceDto.class));

        mockMvc.perform(post(url)
                        .content(objectMapper.writeValueAsString(requestDeductBalanceDto))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Wallet 금액 조회하기 성공")
    void getMoney() throws Exception {
        final String username = "user1";
        final String url = "/wallet";
        doReturn(username).when(securityUtils).getCurrentUsername();
        doReturn(BigDecimal.valueOf(0)).when(walletService).getBalance(username);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(0));
    }
}