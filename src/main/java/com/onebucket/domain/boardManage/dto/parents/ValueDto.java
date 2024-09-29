package com.onebucket.domain.boardManage.dto.parents;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
    public static class GetPost {
        private Long postId;

        //FindPost -> GetPost
        public static GetPost of(FindPost findPost) {
            return GetPost.builder()
                    .postId(findPost.getPostId())
                    .build();
        }
    }

}
