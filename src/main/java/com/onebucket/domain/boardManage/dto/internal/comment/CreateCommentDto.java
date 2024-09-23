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
 * 댓글을 생성할 때 service 레이어로 전해지는 dto이다.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 * Long parentCommentId;
 * String username;
 * } </pre>
 */

@SuperBuilder
@Getter
public class CreateCommentDto extends CommentDto {
    private Long parentCommentId;
    private String username;
}
