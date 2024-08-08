package com.onebucket.domain.boardManage.dto.internal;

import lombok.Builder;
import lombok.Getter;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.internal
 * <br>file name      : DeletePostDto
 * <br>date           : 2024-08-08
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
 * 2024-08-08        jack8              init create
 * </pre>
 */

@Builder
@Getter
public class DeletePostDto {
    private Long id;
    private String username;
}
