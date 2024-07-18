package com.onebucket.domain.memberManage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * <br>package name   : com.onebucket.domain.dto
 * <br>file name      : UpdateMemberRequestDTO
 * <br>date           : 2024-06-24
 * <pre>
 * <span style="color: white;">[description]</span>
 * Endpoint :
 * contain {@code
 * String nickname; }
 * </pre>
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-06-24        SeungHoon              init create
 * </pre>
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NicknameRequestDto {
    @NotBlank(message = "nickname must not be empty")
    private String nickname;
}
