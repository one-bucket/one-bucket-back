package com.onebucket.domain.universityManage.dto.verifiedCode.internal;

import com.onebucket.domain.universityManage.dto.verifiedCode.request.RequestCodeDto;

/**
 * <br>package name   : com.onebucket.domain.mailManage.dto
 * <br>file name      : VerifiedCodeDto
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
public record VerifiedCodeDto(
        String university,
        String universityEmail
) {
    public static VerifiedCodeDto of(RequestCodeDto dto) {
        return new VerifiedCodeDto(dto.university(),dto.universityEmail());
    }
}
