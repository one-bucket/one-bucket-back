package com.onebucket.domain.boardManage.dto.internal.comment;

import com.onebucket.domain.boardManage.dto.parents.CommentDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto
 * <br>file name      : CommentDto
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
@Getter
@SuperBuilder
public class GetCommentDto extends CommentDto {
    private Long commentId;
    private Long authorId;
    private String authorNickname;
    private String imageUrl;

    private LocalDateTime modifiedDate;

    private List<GetCommentDto> replies;
}
