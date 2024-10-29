package com.onebucket.domain.tradeManage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.dto
 * <br>file name      : TradeKeyDto
 * <br>date           : 2024-09-29
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
public class TradeKeyDto {

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class FindTrade {
        private Long tradeId;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class UserTrade extends FindTrade {
        private Long userId;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class SettingChatRoom extends FindTrade {
        private String chatRoomId;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class Finish extends FindTrade {
        private boolean fin;

        public static Finish of(RequestFinish dto) {
            return Finish.builder()
                    .tradeId(dto.getTradeId())
                    .fin(dto.isFin())
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class RequestFinish extends Finish {
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class ExtendDate extends FindTrade {
        private Long date;
    }


}
