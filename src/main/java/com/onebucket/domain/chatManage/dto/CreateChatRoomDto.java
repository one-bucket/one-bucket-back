package com.onebucket.domain.chatManage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * <br>package name   : com.onebucket.domain.chatManage
 * <br>file name      : CreateChatRoomDto
 * <br>date           : 2024-08-01
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
 * 2024-08-01        SeungHoon              init create
 * </pre>
 */
public record CreateChatRoomDto(
        @NotBlank String name,
        @NotNull LocalDateTime createdAt,
        @NotBlank String createdBy,
        @NotNull @Size(min = 1) Set<@NotNull ChatMemberDto> members) {

    public static CreateChatRoomDto of(String name, LocalDateTime createdAt, String createdBy, Set<ChatMemberDto> members) {
        return new CreateChatRoomDto(name, createdAt, createdBy, members);
    }
}
