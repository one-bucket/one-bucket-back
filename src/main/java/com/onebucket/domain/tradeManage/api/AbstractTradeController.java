package com.onebucket.domain.tradeManage.api;

import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.tradeManage.service.TradeService;
import com.onebucket.global.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.api
 * <br>file name      : AbstractTradeController
 * <br>date           : 2024-11-14
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 */
@RequiredArgsConstructor
public abstract class AbstractTradeController<S extends TradeService> {
    protected final S tradeService;
    protected final SecurityUtils securityUtils;
    protected final MemberService memberService;
}
