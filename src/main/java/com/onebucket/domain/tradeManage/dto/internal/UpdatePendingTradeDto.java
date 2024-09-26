package com.onebucket.domain.tradeManage.dto.internal;

import lombok.Builder;
import lombok.Getter;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.dto.internal
 * <br>file name      : UpdatePendingTradeDto
 * <br>date           : 2024-09-26
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
public class UpdatePendingTradeDto {
    private Long id;
    private String item;
    private Long wanted;
    private Long joins;
    private Long price;
    private Long count;
    private String location;
}
