package com.onebucket.domain.chatManage.dto;

import com.onebucket.global.exceptionManage.customException.chatManageException.Exceptions.ChatRoomException;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import jakarta.validation.constraints.Min;
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
        @NotNull @Size(min = 1) Set<@NotNull ChatMemberDto> members,
        @NotNull @Min(1) Integer maxMembers
        ) {

    public static CreateChatRoomDto of(String name, LocalDateTime createdAt, String createdBy, Set<ChatMemberDto> members, Integer maxMembers) {
        return new CreateChatRoomDto(name, createdAt, createdBy, members, maxMembers);
    }
}
