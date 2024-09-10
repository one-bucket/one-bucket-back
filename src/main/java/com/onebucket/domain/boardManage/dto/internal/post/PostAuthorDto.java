package com.onebucket.domain.boardManage.dto.internal.post;

import lombok.Builder;
import lombok.Getter;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.internal.post
 * <br>file name      : PostAuthorDto
 * <br>date           : 2024-09-10
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *    private String username;
 *    private Long postId;
 * } </pre>
 */

@Builder
@Getter
public class PostAuthorDto {
    private String username;
    private Long postId;
}
