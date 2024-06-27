package com.onebucket.domain.memberManage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * <br>package name   : com.onebucket.domain.memberManage.dto
 * <br>file name      : SignInRequstDto
 * <br>date           : 2024-06-27
 * <pre>
 * <span style="color: white;">[description]</span>
 * Endpoint : "/sign-in"
 * contain {@code
 * String username;
 * String password; }
 * </pre>
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-06-27        jack8              init create
 * </pre>
 */

@Getter
@Setter
public class SignInRequestDto {
    @NotBlank(message = "username must not be empty.")
    private String username;

    @NotBlank(message = "password must not be empty.")
    private String password;
}
