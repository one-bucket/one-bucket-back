package com.onebucket.domain.boardManage.dto.postDto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.onebucket.domain.tradeManage.dto.GroupTradeDto;
import com.onebucket.domain.tradeManage.dto.UsedTradeDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.postDto
 * <br>file name      : UsedTradePostDto
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
public class UsedTradePostDto {

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
        private LocalDateTime liftedAt;

        public static InternalThumbnail of(PostDto.InternalThumbnail dto) {
            return InternalThumbnail.builder()
                    .boardId(dto.getBoardId())
                    .title(dto.getTitle())
                    .text(dto.getText())

                    .postId(dto.getPostId())
                    .authorId(dto.getAuthorId())
                    .authorNickname(dto.getAuthorNickname())
                    .createdDate(dto.getCreatedDate())
                    .modifiedDate(dto.getModifiedDate())
                    .imageUrls(dto.getImageUrls())
                    .views(dto.getViews())
                    .likes(dto.getLikes())
                    .build();
        }
    }

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    public static class Thumbnail extends PostDto.Thumbnail {

        @JsonUnwrapped(prefix = "trade_")
        private UsedTradeDto.Info trade;

        private LocalDateTime liftedAt;

        public static Thumbnail of(PostDto.Thumbnail dto) {
            return Thumbnail.builder()
                    .boardId(dto.getBoardId())
                    .title(dto.getTitle())
                    .text(dto.getText())

                    .postId(dto.getPostId())
                    .authorId(dto.getAuthorId())
                    .authorNickname(dto.getAuthorNickname())
                    .createdDate(dto.getCreatedDate())
                    .modifiedDate(dto.getModifiedDate())
                    .imageUrls(dto.getImageUrls())
                    .views(dto.getViews())
                    .likes(dto.getLikes())

                    .build();
        }
    }

    @Getter
    @SuperBuilder
    public static class Info extends PostDto.Info {
        private Long tradeId;

        public static Info of(InternalThumbnail dto) {
            return Info.builder()
                    .boardId(dto.getBoardId())
                    .title(dto.getTitle())
                    .text(dto.getText())

                    .postId(dto.getPostId())
                    .authorId(dto.getAuthorId())
                    .authorNickname(dto.getAuthorNickname())
                    .createdDate(dto.getCreatedDate())
                    .modifiedDate(dto.getModifiedDate())
                    .imageUrls(dto.getImageUrls())
                    .views(dto.getViews())
                    .likes(dto.getLikes())
                    .tradeId(dto.getTrade())
                    .build();
        }
    }


    @Getter
    @Builder
    public static class TradeIdsPageDto {
        private List<Long> tradeIds;
        private Pageable pageable;
    }
}
