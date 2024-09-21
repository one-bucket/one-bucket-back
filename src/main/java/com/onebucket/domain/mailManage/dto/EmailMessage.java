package com.onebucket.domain.mailManage.dto;

/**
 * <br>package name   : com.onebucket.domain.mailManage.dto.internal
 * <br>file name      : EmailMessage
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
public record EmailMessage(
        String to,
        String title
) {
    public static EmailMessage of(final String to, final String title) {
        return new EmailMessage(to, title);
    }
}
