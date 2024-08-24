package com.onebucket.domain.boardManage.dto.internal.post;

import com.onebucket.domain.boardManage.dto.internal.comment.GetCommentDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.internal
 * <br>file name      : PostInfoDto
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
public class PostInfoDto extends PostThumbnailDto {

    private List<GetCommentDto> comments;
}
