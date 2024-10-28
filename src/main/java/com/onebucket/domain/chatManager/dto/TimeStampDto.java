package com.onebucket.domain.chatManager.dto;

import lombok.*;

import java.util.Date;

/**
 * <br>package name   : com.onebucket.domain.chatManager.mongo
 * <br>file name      : TimeStampDto
 * <br>date           : 2024-10-22
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
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeStampDto {
    private String roomId;
    private Date time;
}
