package com.onebucket.domain.memberManage.dto.internal;

import com.onebucket.domain.memberManage.dto.RequestSetPasswordDto;

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
public record SetPasswordDto(
        String username,
        String oldPassword,
        String newPassword
) {
    public static SetPasswordDto of(String username, RequestSetPasswordDto dto) {
        return new SetPasswordDto(username, dto.getOldPassword(), dto.getNewPassword());
    }
}
