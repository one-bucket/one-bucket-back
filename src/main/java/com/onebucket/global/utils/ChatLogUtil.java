package com.onebucket.global.utils;

import com.onebucket.domain.chatManage.domain.ChatMessage;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * <br>package name   : com.onebucket.global.utils
 * <br>file name      : ChatLogUtil
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
@Component
public class ChatLogUtil {
    /**
     * ChatMessage를 log 형식에 맞게 재구성한다.
     *
     * @param message
     * @return [DATE and Time] User (Chat_Type): ChatMessage (Room ID: ~~)
     */
    public static String formatChatMessage(ChatMessage message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = message.getCreateAt().format(formatter);
        return String.format("[%s] %s (%s): %s (Room ID: %s)",
                timestamp, message.getSender(), message.getType(), message.getMessage(), message.getRoomId());
    }

    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }
}
