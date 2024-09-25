package com.onebucket.domain.tradeManage.dto.internal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.dto.internal
 * <br>file name      : UserTradeDto
 * <br>date           : 9/25/24
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
@Builder
@Getter
@Setter
public class UserTradeDto {
    private Long userId;
    private Long tradeId;
}
