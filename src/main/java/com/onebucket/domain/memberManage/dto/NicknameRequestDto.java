package com.onebucket.domain.memberManage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(min = 4, max = 14, message = "size of nickname must be over 4 under 14")
    private String nickname;
}
