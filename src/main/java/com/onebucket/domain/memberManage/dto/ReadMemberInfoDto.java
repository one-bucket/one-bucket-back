package com.onebucket.domain.memberManage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <br>package name   : com.onebucket.domain.memberManage.dto
 * <br>file name      : ReadMemberInfoDto
 * <br>date           : 2024-07-01
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
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-07-01        jack8              init create
 * </pre>
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReadMemberInfoDto {
    private String username;
    private String nickname;
    private String university;
}
