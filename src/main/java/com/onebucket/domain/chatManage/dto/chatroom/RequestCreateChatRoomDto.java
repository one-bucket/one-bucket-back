package com.onebucket.domain.chatManage.dto.chatroom;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * <br>package name   : com.onebucket.domain.chatManage.dto.chatroom
 * <br>file name      : RequestCreateChatRoomDto
 * <br>date           : 2024-09-11
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
 * 2024-09-11        SeungHoon              init create
 * </pre>
 */
public record RequestCreateChatRoomDto (
        @NotBlank String name,
        @NotBlank String createdBy,
        @NotNull @Size(min = 1) Set<@NotNull ChatMemberDto> members,
        @NotNull @Min(1) Integer maxMembers
) {
}
