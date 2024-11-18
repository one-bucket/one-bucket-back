package com.onebucket.domain.memberManage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <br>package name   : com.onebucket.domain.memberManage.dto
 * <br>file name      : RefreshTokenDto
 * <br>date           : 2024-07-01
 * <pre>
 * <span style="color: white;">[description]</span>
 * Endpoint : "/refresh-token"
 * contain {@code
 * private String refreshToken;}
 * </pre>
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-07-01        jack8              init create
 * </pre>
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenDto {
    @NotBlank(message = "refreshToken must not be null")
    private String refreshToken;
}
