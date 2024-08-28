package com.onebucket.domain.boardManage.dto.internal.post;

import com.onebucket.domain.boardManage.dto.parents.PostDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.internal
 * <br>file name      : PostsDto
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
public class PostThumbnailDto extends PostDto {
    private Long postId;
    private String authorNickname;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
