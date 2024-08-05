package com.onebucket.domain.memberManage.dto;

import com.onebucket.domain.memberManage.domain.Profile;

import java.time.LocalDate;

/**
 * <br>package name   : com.onebucket.domain.memberManage.dto
 * <br>file name      : ChatMemberDto
 * <br>date           : 2024-07-30
 * <pre>
 * <span style="color: white;">[description]</span>
 * 채팅방에 속한 멤버들 정보를 담는다.
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
 * 2024-07-30        SeungHoon              init create
 * </pre>
 */
public record ChatMemberDto(
        String nickname
) {
    public static ChatMemberDto from(String nickname) {
        return new ChatMemberDto(nickname);
    }
}
