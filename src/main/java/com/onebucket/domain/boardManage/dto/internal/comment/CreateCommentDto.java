package com.onebucket.domain.boardManage.dto.internal.comment;

import com.onebucket.domain.boardManage.dto.parents.CommentDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.internal
 * <br>file name      : CreateCommentDto
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

@SuperBuilder
@Getter
public class CreateCommentDto extends CommentDto {
    private Long parentCommentId;
    private String username;
}
