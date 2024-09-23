package com.onebucket.domain.boardManage.dto.internal.post;

import lombok.Builder;
import lombok.Getter;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.internal.post
 * <br>file name      : SaveImageDto
 * <br>date           : 9/23/24
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

@Builder
@Getter
public class SaveImageDto {
    private Long postId;
    private String imageName;
    private String fileExtension;
}
