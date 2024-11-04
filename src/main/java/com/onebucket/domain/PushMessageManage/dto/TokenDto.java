package com.onebucket.domain.PushMessageManage.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <br>package name   : com.onebucket.domain.PushMessageManage.dto
 * <br>file name      : TokenDto
 * <br>date           : 2024-11-03
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 */
public class TokenDto {


    @Builder
    @Getter
    public static class Info {
        private Long userId;
        private String token;
    }
}
