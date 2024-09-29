package com.onebucket.domain.boardManage.dto.parents;

import com.onebucket.domain.boardManage.dto.internal.comment.GetCommentDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    public static class Create extends BasePost {
        private String username;
        private Long univId;
    }

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    public static class Thumbnail extends BasePost {

        private Long postId;
        private String authorNickname;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;

        private Long views;
        private Long likes;
        private Long commentsCount;

        private boolean isImageExist;
        private String thumbnailImage;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class Info extends Thumbnail {
        private List<GetCommentDto> comments;
    }

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    public static class ResponseInfo extends Info {
        private boolean isUserAlreadyLikes;


        public static ResponseInfo of(Info info) {
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
                    .build();
        }
    }


}
