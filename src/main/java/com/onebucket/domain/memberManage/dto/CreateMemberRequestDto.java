package com.onebucket.domain.memberManage.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <br>package name   : com.onebucket.domain.dto
 * <br>file name      : CreateMemberRequestDTO
 * <br>date           : 2024-06-24
 * <pre>
 * <span style="color: white;">[description]</span>
 * Endpoint : "/register", {@link com.onebucket.domain.memberManage.service.MemberService MemberService}
 * contain {@code
 * String username;
 * String password;
 * String nickname;}
 * </pre>
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
public class CreateMemberRequestDto {
    @NotBlank(message = "username must not be empty")
    @Size(min = 5, max = 20, message = "size of username must be over 8, under 20")
    private String username;

    /**
     * 최소 하나의 대문자
     * 최소 하나의 소문자
     * 최소 하나의 숫자
     * 최소 하나의 @$!%*?& 중 하나의 특수문자
     */
    @NotBlank(message = "password must not be empty")
    @Size(min = 8, max = 20, message = "size of password must be over 8, under 20")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    private String password;

    @NotBlank(message = "nickname must not be empty")
    @Size(min = 4, max = 14, message = "size of nickname must be over 4 under 14")
    private String nickname;

}
