package com.onebucket.domain.chatManager.dto;

import com.onebucket.domain.tradeManage.dto.BaseTradeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <br>package name   : com.onebucket.domain.chatManager.dto
 * <br>file name      : ChatRoomInfoDto
 * <br>date           : 2024-11-17
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
public class ChatRoomInfoDto<D extends BaseTradeDto.Info> {

    private ChatRoomDto.Info chatRoom;

    private D trade;

}
