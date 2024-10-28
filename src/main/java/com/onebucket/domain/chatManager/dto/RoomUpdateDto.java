package com.onebucket.domain.chatManager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <br>package name   : com.onebucket.domain.chatManager.dto
 * <br>file name      : RoomUpdateDto
 * <br>date           : 2024-10-27
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
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomUpdateDto {
    private String roomId;
    private String recentMessage;
    private Date recentMessageTime;
}
