package com.onebucket.domain.tradeManage.dto;

import com.onebucket.domain.tradeManage.entity.BaseTrade;
import com.onebucket.domain.tradeManage.entity.TradeTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.dto
 * <br>file name      : UsedTradeDto
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
public class UsedTradeDto {

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Base extends BaseTradeDto.Base {

    }

    @SuperBuilder
    @Getter
    @NoArgsConstructor
    public static class Create extends BaseTradeDto.Create {

    }

    @SuperBuilder
    @Getter
    @NoArgsConstructor
    public static class Info extends BaseTradeDto.Info {
        private boolean isReserve;
    }

    @Getter
    public static class UpdateTrade extends BaseTradeDto.UpdateTrade {

        public UpdateTrade(BaseTradeDto.Base base, BaseTrade trade, TradeTag tag) {
            super(base, trade, tag);
        }
    }
}
