package com.onebucket.domain.boardManage.dto.parents;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Pageable;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.parents
 * <br>file name      : ValueDto
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
public class ValueDto {

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class BasePost {
        private Long postId;
        private Long userId;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class FindPost extends BasePost {

    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class FindComment {
        private Long postId;
        private Long commentId;

    }

    @Getter
    @SuperBuilder
    public static class GetPost {
        private Long postId;

        //FindPost -> GetPost
        public static GetPost of(FindPost findPost) {
            return GetPost.builder()
                    .postId(findPost.getPostId())
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    public static class PageablePost {
        private Long boardId;
        private Pageable pageable;
    }

    @Getter
    @Builder
    public static class AuthorPageablePost {
        private Long userId;
        private Pageable pageable;
    }

    @Getter
    @SuperBuilder
    public static class SearchPageablePost extends PageablePost {
        //1 is for title, 2 is for text, 3 is for title + text
        //don't know what to do when tag search...
        //cause no implement yet
        private Integer option;
        private String keyword;
    }
}
