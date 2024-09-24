package com.onebucket.domain.universityManage.dto.verifiedCode.internal;

import com.onebucket.domain.universityManage.dto.verifiedCode.request.RequestCodeCheckDto;

/**
 * <br>package name   : com.onebucket.domain.mailManage.dto.internal
 * <br>file name      : VerifiedCodeCheckDto
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
public record VerifiedCodeCheckDto(
        String universityEmail,
        String verifiedCode
) {
    public static VerifiedCodeCheckDto of(RequestCodeCheckDto dto) {
        return new VerifiedCodeCheckDto(dto.verifiedCode(),dto.universityEmail());
    }
}
