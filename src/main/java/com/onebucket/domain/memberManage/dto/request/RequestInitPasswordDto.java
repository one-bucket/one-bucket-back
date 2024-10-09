package com.onebucket.domain.memberManage.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
 * <br>package name   : com.onebucket.domain.memberManage.dto.request
 * <br>file name      : RequestResetPasswordDto
 * <br>date           : 2024-10-01
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
 * 2024-10-01        SeungHoon              init create
 * </pre>
 */
public record RequestInitPasswordDto(
        @NotNull
        String username,
        @Email
        String email
) {
}
