package com.onebucket.domain.memberManage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * <br>package name   : com.onebucket.domain.memberManage.dto
 * <br>file name      : SetPasswordDto
 * <br>date           : 2024-07-08
 * <pre>
 * <span style="color: white;">[description]</span>
 * 비밀번호를 새로 설정하거나, 임시 생성된 비밀번호를 전달하기 위한 dto
 * </pre>
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestSetPasswordDto {

    @NotBlank(message = "password must not be empty")
    @Size(min = 8, max = 20, message = "size of password must be over 8, under 20")
    private String oldPassword;

    @NotBlank(message = "password must not be empty")
    @Size(min = 8, max = 20, message = "size of password must be over 8, under 20")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    private String newPassword;
}
