package com.onebucket.domain.tradeManage.dto;

import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.tradeManage.entity.PendingTrade;
import com.onebucket.domain.tradeManage.entity.TradeTag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.dto
 * <br>file name      : TradeDto
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
public class TradeDto {

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class BaseTrade {
        private String item;
        private Long wanted;
        private Long price;
        private Long count;
        private String location;
        private String linkUrl;
        private String tag;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class Create extends BaseTrade {
        private Long ownerId;
        private Long dueDays;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class Info extends BaseTrade {
        private Long id;
        private Long userId;
        private LocalDateTime dueDate;

        private Long joins;
        private List<Long> memberIds;
        private boolean isFin;

        private LocalDateTime startTradeAt;

        public static Info of(PendingTrade entity) {
            List<Long> ids = entity.getMembers().stream().map(Member::getId).toList();

            return Info.builder()
                    .item(entity.getItem())
                    .wanted(entity.getWanted())
                    .price(entity.getPrice())
                    .count(entity.getCount())

                    .id(entity.getId())
                    .userId(entity.getOwner().getId())
                    .tag(entity.getTradeTag().getName())
                    .linkUrl(entity.getLinkUrl())
                    .location(entity.getLocation())
                    .dueDate(entity.getDueDate())
                    .joins(entity.getJoins())
                    .memberIds(ids)
                    .isFin(entity.isFin())
                    .startTradeAt(entity.getStartTradeAt())
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class Update extends BaseTrade {
        private Long id;
    }
    /**
     * @param dto
     * @return
     */
    public static PendingTrade to(Create dto, Member member, TradeTag tag) {
        LocalDateTime now = LocalDateTime.now();
        return PendingTrade.builder()
                .owner(member)
                .item(dto.getItem())
                .tradeTag(tag)
                .linkUrl(dto.getLinkUrl())
                .wanted(dto.getWanted())
                .startTradeAt(now)
                .price(dto.getPrice())
                .count(dto.getCount())
                .dueDate(now.plusDays(dto.getDueDays()))
                .location(dto.getLocation())
                .isFin(false)
                .build();

    }
}
