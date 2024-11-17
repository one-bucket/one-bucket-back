package com.onebucket.domain.tradeManage.dto;

import com.onebucket.domain.tradeManage.entity.GroupTrade;
import com.onebucket.domain.tradeManage.entity.TradeTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.minidev.json.annotate.JsonIgnore;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.dto
 * <br>file name      : GroupTradeDto
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
public class GroupTradeDto {

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Base extends BaseTradeDto.Base {
        private Long wanted;
        private Long count;
    }

    @SuperBuilder
    @Getter
    @NoArgsConstructor
    public static class Create extends BaseTradeDto.Create {
        private Long wanted;
        private Long count;
    }

    @SuperBuilder
    @Getter
    @NoArgsConstructor
    public static class Info extends BaseTradeDto.Info {
        private List<JoinMember> joinMember;
        private Long wanted;
        private Long joins;
        private Long count;

        private Long chatRoomId;
    }

    @SuperBuilder
    @Getter
    @NoArgsConstructor
    public static class ResponseInfo extends Info {

        @JsonIgnore
        private List<JoinMember> joinMember;
    }

    @Getter
    public static class UpdateTrade extends BaseTradeDto.UpdateTrade {
        private Long wanted;
        private Long count;

        public UpdateTrade(Base base, GroupTrade trade, TradeTag tag) {
            super(base, trade, tag);
            this.wanted = base.getWanted();
            this.count = base.getCount();
        }
    }
}
