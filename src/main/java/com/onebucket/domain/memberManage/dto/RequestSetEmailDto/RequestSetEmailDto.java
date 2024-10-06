package com.onebucket.domain.memberManage.dto.RequestSetEmailDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
 * <br>package name   : com.onebucket.domain.memberManage.dto.RequestSetEmailDto
 * <br>file name      : RequestSetEmailDto
 * <br>date           : 2024-09-24
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
 * 2024-09-24        SeungHoon              init create
 * </pre>
 */
public record RequestSetEmailDto(
        @NotNull
        @Email(message = "Invalid email format")
        String email
) {
}
