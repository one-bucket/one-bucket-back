package com.onebucket.domain.boardManage.dto.postDto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.postDto
 * <br>file name      : GroupTradePostDto
 * <br>date           : 2024-11-17
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
public class GroupTradePostDto {

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class RequestCreate extends Create {

        @JsonIgnore
        private Long userId;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class Create extends PostDto.Create {
        private Long tradeId;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @Setter
    public static class InternalThumbnail extends PostDto.InternalThumbnail {

        private Long trade;

//        @JsonUnwrapped(prefix = "trade_")
//        private GroupTradeDto.ResponseInfo tradeInfo;
    }

    @Getter
    @SuperBuilder
    public static class Info extends PostDto.Info {
        private Long tradeId;
    }


    @Getter
    @Builder
    public static class TradeIdsPageDto {
        private List<Long> tradeIds;
        private Pageable pageable;
    }
}
