package com.onebucket.domain.boardManage.dto.internal.post;

import lombok.Builder;
import lombok.Getter;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.internal
 * <br>file name      : GetPostDto
 * <br>date           : 2024-08-15
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-08-15        jack8              init create
 * </pre>
 */
@Builder
@Getter
public class GetPostDto {
    private String username;
    private Long postId;
}
