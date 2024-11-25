package com.onebucket.domain.PushMessageManage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.PushMessageManage.dto
 * <br>file name      : MessageLogDto
 * <br>date           : 11/5/24
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
public class MessageLogDto {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Info {
        private String title;
        private String body;
        private String image;
    }


}
