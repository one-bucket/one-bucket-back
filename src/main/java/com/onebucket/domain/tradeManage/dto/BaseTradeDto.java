package com.onebucket.domain.tradeManage.dto;

import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.tradeManage.entity.BaseTrade;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

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
    public static class Base {
        private String item;
        private Long price;
        private String location;
        private String linkUrl;
        private String tag;
    }

    @SuperBuilder
    @Getter
    @NoArgsConstructor
    public static class Create extends Base {
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

        private JoinMember joinMember;
        private boolean isFin;

        private LocalDateTime startTradeAt;

        @Getter
        @Builder
        private static class JoinMember {
            private Long id;
            private String nickname;
            private String imageUrl;
        }
    }

    @Getter
    @SuperBuilder
    public static class Update extends Base {
        private Long tradeId;
    }
}
