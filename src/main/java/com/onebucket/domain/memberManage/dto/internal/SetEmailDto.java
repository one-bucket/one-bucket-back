package com.onebucket.domain.memberManage.dto.internal;

import com.onebucket.domain.memberManage.dto.RequestSetEmailDto.RequestSetEmailDto;

/**
 * <br>package name   : com.onebucket.domain.memberManage.dto.internal
 * <br>file name      : SetEmailDto
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
public record SetEmailDto(
        String username,
        String email
) {
    public static SetEmailDto of(String username, RequestSetEmailDto dto) {
        return new SetEmailDto(username, dto.email());
    }
}
