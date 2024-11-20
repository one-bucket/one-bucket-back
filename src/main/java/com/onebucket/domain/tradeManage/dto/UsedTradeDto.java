package com.onebucket.domain.tradeManage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


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

        public static Info of(BaseTradeDto.Info dto) {
            return Info.builder()
                    .item(dto.getItem())
                    .price(dto.getPrice())
                    .location(dto.getLocation())
                    .linkUrl(dto.getLinkUrl())
                    .id(dto.getId())
                    .userId(dto.getUserId())
                    .dueDate(dto.getDueDate())
                    .tag(dto.getTag())
                    .isFin(dto.isFin())
                    .createAt(dto.getCreateAt())
                    .updateAt(dto.getUpdateAt())
                    .build();
        }
    }

    @SuperBuilder
    @Getter
    @NoArgsConstructor
    public static class ListedInfo extends BaseTradeDto.Info {

        private boolean isReserved;

        public static ListedInfo of(UsedTradeDto.Info dto) {
            return ListedInfo.builder()
                    .item(dto.getItem())
                    .price(dto.getPrice())
                    .location(dto.getLocation())
                    .linkUrl(dto.getLinkUrl())

                    .id(dto.getId())
                    .userId(dto.getUserId())
                    .dueDate(dto.getDueDate())
                    .tag(dto.getTag())
                    .isFin(dto.isFin())
                    .createAt(dto.getCreateAt())
                    .updateAt(dto.getUpdateAt())
                    .isReserved(dto.isReserve())
                    .build();
        }
    }

    @Getter
    public static class UpdateTrade extends BaseTradeDto.UpdateTrade {


    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class Update extends BaseTradeDto.Update {

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestCreateChat {
        private Long tradeId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseCreateChat {
        private String roomId;

        private String ownerNickname;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestReserve {
        private Long tradeId;
        private Long joinerId;
    }

}
