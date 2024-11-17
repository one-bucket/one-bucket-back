package com.onebucket.domain.boardManage.dto.postDto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.onebucket.domain.tradeManage.dto.GroupTradeDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Pageable;

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
    public static class RequestCreate {
        private PostDto.Create post;
        private GroupTradeDto.Create trade;
        private String chatRoomName;

    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class RequestUpdate {
        private PostDto.Update post;
        private GroupTradeDto.Update trade;

    }

    @Getter
    @Setter
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
    }

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    public static class Thumbnail extends PostDto.Thumbnail {

        @JsonUnwrapped(prefix = "trade_")
        private GroupTradeDto.ListedInfo trade;
    }

    @Getter
    @SuperBuilder
    public static class Info extends PostDto.Info {
        private Long tradeId;
    }

    @Getter
    @SuperBuilder
    @Setter
    public static class ResponseInfo extends PostDto.ResponseInfo {

        @JsonUnwrapped(prefix = "trade_")
        private GroupTradeDto.Info trade;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ResponseCreate {
        private Long postId;
        private String chatRoomId;
    }


    @Getter
    @Builder
    public static class TradeIdsPageDto {
        private List<Long> tradeIds;
        private Pageable pageable;
    }
}
