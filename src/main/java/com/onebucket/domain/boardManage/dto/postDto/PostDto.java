package com.onebucket.domain.boardManage.dto.postDto;

import com.onebucket.domain.boardManage.dto.internal.comment.GetCommentDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.parents
 * <br>file name      : PostDto
 * <br>date           : 2024-08-08
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *     private Long boardId;
 *     private String title;
 *     private String text;
 * } </pre>
 */

public class PostDto {

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    public static class BasePost {
        private Long boardId;
        private String title;
        private String text;
    }

    //------------------------------------------------

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class RequestCreate extends BasePost {

    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class Create extends RequestCreate {
        private Long userId;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update extends BasePost {
        private Long postId;
    }

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    public static class InternalThumbnail extends BasePost {

        private Long postId;

        private Long authorId;
        private String authorNickname;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;
        private List<String> imageUrls;

        private Long views;
        private Long likes;

    }


    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    public static class Thumbnail extends InternalThumbnail {
        private Long likes;
        private Long commentsCount;

        public static Thumbnail of(InternalThumbnail dto) {
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
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    public static class Info extends Thumbnail {
        private List<GetCommentDto> comments;

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
                    .build();
        }

    }

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    public static class ResponseInfo extends Info {
        private boolean isUserAlreadyLikes;

        public static ResponseInfo of(Info dto) {
            return ResponseInfo.builder()

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
                    .comments(dto.getComments())
                    .commentsCount(dto.getCommentsCount())
                    .build();
        }
    }


}
