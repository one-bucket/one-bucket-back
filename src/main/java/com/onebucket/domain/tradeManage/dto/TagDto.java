package com.onebucket.domain.tradeManage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.dto
 * <br>file name      : TagDto
 * <br>date           : 2024-10-06
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
public class TagDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TagName {
        private String name;
    }
}
