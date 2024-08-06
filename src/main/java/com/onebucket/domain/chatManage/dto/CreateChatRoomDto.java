package com.onebucket.domain.chatManage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

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
public record CreateChatRoomDto(@NotBlank String name, @NotNull LocalDateTime createdAt, @NotBlank String createdBy) {
    public static CreateChatRoomDto of(String name, LocalDateTime createdAt, String createdBy) {
        return new CreateChatRoomDto(name, createdAt, createdBy);
    }
}
