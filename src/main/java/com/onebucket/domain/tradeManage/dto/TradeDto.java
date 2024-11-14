package com.onebucket.domain.tradeManage.dto;

import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.tradeManage.entity.GroupTrade;
import com.onebucket.domain.tradeManage.entity.TradeTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
        private Long dueDays;
        private Long ownerId;

        public static Create of(RequestCreate dto, Long ownerId) {
            return Create.builder()
                    .item(dto.getItem())
                    .wanted(dto.getWanted())
                    .price(dto.getPrice())
                    .count(dto.getCount())
                    .location(dto.getLocation())
                    .linkUrl(dto.getLinkUrl())
                    .tag(dto.getTag())
                    .dueDays(dto.getDueDays())

                    .ownerId(ownerId)
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class RequestCreate extends BaseTrade {
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
        private List<String> nickNames;
        private boolean isFin;

        private LocalDateTime startTradeAt;

        public static Info of(GroupTrade entity) {
            List<String> nicknames = entity.getJoiners().stream().map(Member::getNickname).toList();

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
                    .nickNames(nicknames)
                    .isFin(entity.isFin())
                    .startTradeAt(entity.getStartTradeAt())
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class ResponseInfo extends Info {

        public static ResponseInfo of(Info dto) {
            return ResponseInfo.builder()
                    .item(dto.getItem())
                    .wanted(dto.getWanted())
                    .price(dto.getPrice())
                    .count(dto.getCount())

                    .id(dto.getId())
                    .userId(dto.getUserId())
                    .tag(dto.getTag())
                    .linkUrl(dto.getLinkUrl())
                    .location(dto.getLocation())
                    .dueDate(dto.getDueDate())
                    .joins(dto.getJoins())
                    .nickNames(dto.getNickNames())
                    .isFin(dto.isFin())
                    .startTradeAt(dto.getStartTradeAt())
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class Update extends BaseTrade {
        private Long tradeId;
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseJoinTrade {
        private Long tradeId;
        private String chatRoomId;
    }

    public static GroupTrade to(Create dto, Member member, TradeTag tag) {
        LocalDateTime now = LocalDateTime.now();
        return com.onebucket.domain.tradeManage.entity.GroupTrade.builder()
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
