package com.onebucket.domain.memberManage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
public class SetPasswordDto {

    private String password;
}
