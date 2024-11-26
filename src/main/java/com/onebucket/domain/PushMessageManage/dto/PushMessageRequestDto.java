package com.onebucket.domain.PushMessageManage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <br>package name   : com.onebucket.domain.PushMessageManage.dto
 * <br>file name      : PushMessageRequestDto
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
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushMessageRequestDto {
    private Long userId;
    private String title;
    private String body;
}
