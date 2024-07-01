package com.onebucket.domain.memberManage.dto;

import jakarta.validation.constraints.NotBlank;
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
    private String username;

    @NotBlank(message = "password must not be empty")
    private String password;

    @NotBlank(message = "nickname must not be empty")
    private String nickname;
}
