package com.onebucket.global.utils;

import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.global.exceptionManage.customException.chatManageException.ChatManageException;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.onebucket.domain.chatManage.Const.LOG_DIRECTORY;

/**
 * <br>package name   : com.onebucket.global.utils
 * <br>file name      : ChatLogUtil
 * <br>date           : 2024-07-08
 * <pre>
 * <span style="color: white;">[description]</span>
 * 채팅과 관련된 여러가지 Utils
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 * public static String formatChatMessage(ChatMessage message)
 * public static String getCurrentDate()
 * public synchronized void saveChatLog(ChatMessage message)
 * private static void ensureLogDirectoryExists()
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
@Slf4j
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

    /**
     * local에 존재하는 chat_logs directory에 log를 저장한다.
     * @param message
     */
    public synchronized void saveChatLog(ChatMessage message) {
        ensureLogDirectoryExists();
        String fileName = LOG_DIRECTORY + getCurrentDate() + "_chatLog.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(formatChatMessage(message));
            writer.newLine();
        } catch (IOException e) {
            throw new ChatManageException(ChatErrorCode.SAVE_LOG_FAILED);
        }
    }

    /**
     * log 저장하는 directory가 없으면 만든다.
     */
    private static void ensureLogDirectoryExists() {
        File dir = new File(LOG_DIRECTORY);
        if (!dir.exists()) {
            log.info("Create directory: {}", LOG_DIRECTORY);
            dir.mkdirs();
        }
    }
}
