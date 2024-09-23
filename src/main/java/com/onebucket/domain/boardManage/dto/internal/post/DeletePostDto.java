package com.onebucket.domain.boardManage.dto.internal.post;

import lombok.Builder;
import lombok.Getter;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.internal
 * <br>file name      : DeletePostDto
 * <br>date           : 2024-08-08
 * <pre>
 * <span style="color: white;">[description]</span>
 * post를 삭제할 때 사용하는 dto.
 * {@code
 * Long id;
 * String username;
 * }
 *
 * </pre>
 */

@Builder
@Getter
public class DeletePostDto {
    private Long id;
    private Long memberId;
}
