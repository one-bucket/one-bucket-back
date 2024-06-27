package com.onebucket.domain.memberManage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <br>package name   : com.onebucket.domain.dto
 * <br>file name      : UpdateMemberRequestDTO
 * <br>date           : 2024-06-24
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
 * 2024-06-24        SeungHoon              init create
 * </pre>
 */
@Builder
@Getter
@Setter
public class UpdateNicknameRequestDto {
    @NotBlank(message = "nickname must not be empty")
    private String nickname;
}
