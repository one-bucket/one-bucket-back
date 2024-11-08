package com.onebucket.domain.boardManage.dto.parents;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.onebucket.domain.tradeManage.dto.TradeDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.parents
 * <br>file name      : MarketPostDto
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
public class MarketPostDto {

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class BaseMarketPost extends PostDto.BasePost {

    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class RequestCreate extends PostDto.RequestCreate {
        private Long tradeId;
    }

    @Getter
    @SuperBuilder
    public static class Create extends PostDto.Create {
        private Long tradeId;

        public static Create of(RequestCreate dto, String username, Long univId, Long tradeId) {
            return Create.builder()
                    .boardId(dto.getBoardId())
                    .text(dto.getText())
                    .title(dto.getTitle())
                    .username(username)
                    .univId(univId)
                    .tradeId(tradeId)
                    .build();
        }
    }

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    public static class Thumbnail extends PostDto.Thumbnail {
        private Long tradeId;

        @JsonUnwrapped(prefix = "trade_")
        private TradeDto.ResponseInfo tradeInfo;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class Info extends PostDto.Info {
        private Long tradeId;
    }

    @Getter
    @SuperBuilder
    public static class ResponseInfo extends PostDto.ResponseInfo {
        private Long tradeId;

        @JsonUnwrapped(prefix = "trade_")
        private TradeDto.ResponseInfo tradeInfo;

        public static ResponseInfo of(Info info, TradeDto.ResponseInfo tradeInfo) {
            return ResponseInfo.builder()
                    .postId(info.getPostId())
                    .boardId(info.getBoardId())
                    .authorNickname(info.getAuthorNickname())
                    .createdDate(info.getCreatedDate())
                    .modifiedDate(info.getModifiedDate())
                    .comments(info.getComments())
                    .title(info.getTitle())
                    .text(info.getText())
                    .likes(info.getLikes())
                    .views(info.getViews())
                    .tradeId(info.getTradeId())

                    .tradeInfo(tradeInfo)
                    .build();
        }

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseCreatePostDto {
        private Long postId;
        private String chatRoomId;
    }

}
