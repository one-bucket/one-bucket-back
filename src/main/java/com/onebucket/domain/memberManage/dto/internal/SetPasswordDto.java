package com.onebucket.domain.memberManage.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <br>package name   : com.onebucket.domain.memberManage.dto.internal
 * <br>file name      : SetPasswordDto
 * <br>date           : 2024-10-04
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
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-10-04        SeungHoon              init create
 * </pre>
 */
@Getter
@Builder
@AllArgsConstructor
public class SetPasswordDto {
    private String username;
    private String oldPassword;
    private String newPassword;
}
