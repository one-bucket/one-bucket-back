package com.onebucket.domain.universityManage.dto.verifiedCode.response;

/**
 * <br>package name   : com.onebucket.domain.mailManage.dto
 * <br>file name      : ResponseEmailDto
 * <br>date           : 2024-09-20
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
 * 2024-09-20        SeungHoon              init create
 * </pre>
 */
public record ResponseCodeDto (
        String verifiedCode
) {
    public static ResponseCodeDto of(String verifiedCode) {
        return new ResponseCodeDto(verifiedCode);
    }
}
