package com.onebucket.domain.PushMessageManage.dto;

import lombok.Builder;
import lombok.Getter;

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
public class PushMessageRequestDto {
    private String targetToken;
    private String title;
    private String body;
}
