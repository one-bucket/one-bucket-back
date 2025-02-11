package com.onebucket.domain.tradeManage.dto;

import com.onebucket.domain.tradeManage.entity.BaseTrade;
import com.onebucket.domain.tradeManage.entity.TradeTag;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.dto
 * <br>file name      : BaseTradeDto
 * <br>date           : 11/13/24
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
public class BaseTradeDto {

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Base {
        private String item;
        private Long price;
        private String location;
        private String linkUrl;
    }

    @SuperBuilder
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Create extends Base {
        private String tag;
        private Long dueDate;
        private Long ownerId;
    }
    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class Info extends Base {
        private Long id;
        private Long userId;
        private LocalDateTime dueDate;
        private String tag;
        private boolean isFin;

        private LocalDateTime createAt;
        private LocalDateTime updateAt;

        @Getter
        @Builder
        public static class JoinMember {
            private Long id;
            private String nickname;
            private String imageUrl;
        }
    }

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    public static class Update extends Base {
        private String tag;
        private Long tradeId;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class UpdateTrade extends Base {

        private BaseTrade trade;
        private TradeTag tag;

        public UpdateTrade(Base base, BaseTrade trade, TradeTag tag) {
            super(base.getItem(), base.getPrice(), base.getLocation(), base.getLinkUrl());
            this.trade = trade;
            this.tag = tag;
        }
    }


}
