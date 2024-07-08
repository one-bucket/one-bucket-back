package com.onebucket.domain.chatManage.domain;

import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * <br>package name   : com.onebucket.domain.chatManage.domain
 * <br>file name      : ChatRoomn
 * <br>date           : 2024-07-08
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
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-07-08        SeungHoon              init create
 * </pre>
 */
@Getter
@Builder
public class ChatRoom implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String roomId;
    private String name;
}
