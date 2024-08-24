package com.onebucket.domain.boardManage.dto.parents;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.parents
 * <br>file name      : CommentDto
 * <br>date           : 2024-08-21
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
 * 2024-08-21        jack8              init create
 * </pre>
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public abstract class CommentDto {
    private String text;
    private Long postId;
}
