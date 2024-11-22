package com.onebucket.domain.boardManage.dto.postDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Pageable;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.postDto
 * <br>file name      : PostKeyDto
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
public class PostKeyDto {

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostKey {
        private Long postId;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserPost extends PostKey {
        private Long userId;
    }


    @Getter
    @SuperBuilder
    public static class BoardPage {
        private Pageable pageable;
        private Long boardId;
    }

    @Getter
    @SuperBuilder
    public static class UserPage {
        private Pageable pageable;
        private Long userId;
    }

    @Getter
    @SuperBuilder
    public static class AuthorPage extends BoardPage {
        private Long userId;
    }

    @Getter
    @SuperBuilder
    public static class SearchPage extends BoardPage {
        //1 is for title, 2 is for text, 3 is for title + text
        //don't know what to do when tag search...
        //cause no implement yet
        private Integer option;
        private String keyword;
    }

    @SuperBuilder
    @Getter
    public static class CommentKey extends PostKey {
        private Long commentId;
    }


}
