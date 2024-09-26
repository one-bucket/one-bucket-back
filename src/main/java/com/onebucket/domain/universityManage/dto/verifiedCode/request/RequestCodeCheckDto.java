package com.onebucket.domain.universityManage.dto.verifiedCode.request;

import jakarta.validation.constraints.Email;

/**
 * <br>package name   : com.onebucket.domain.mailManage.dto.request
 * <br>file name      : RequestCodeCheckDto
 * <br>date           : 2024-09-21
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
 * 2024-09-21        SeungHoon              init create
 * </pre>
 */
public record RequestCodeCheckDto(
        @Email
        String universityEmail,
        String verifiedCode
) {
}
