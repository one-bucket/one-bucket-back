package com.onebucket.domain.tradeManage.dto.request;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.dto.request
 * <br>file name      : TradeFinishDto
 * <br>date           : 9/27/24
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

public class TradeFinishDto {

    @Getter
    @SuperBuilder
    public static class RequestTradeFinishDto {
        private Long tradeId;
        private boolean value;

        public static InternalTradeFinishDto of(RequestTradeFinishDto dto) {
            return InternalTradeFinishDto.builder()
                    .tradeId(dto.getTradeId())
                    .value(dto.isValue())
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    public static class InternalTradeFinishDto extends RequestTradeFinishDto {
    }



}
