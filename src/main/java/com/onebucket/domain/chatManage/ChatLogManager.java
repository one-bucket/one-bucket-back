package com.onebucket.domain.chatManage;

import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.global.exceptionManage.customException.chatManageException.ChatManageException;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.onebucket.domain.chatManage.Const.LOG_DIRECTORY;
import static com.onebucket.global.utils.ChatLogUtil.formatChatMessage;
import static com.onebucket.global.utils.ChatLogUtil.getCurrentDate;

/**
 * <br>package name   : com.onebucket.domain.chatManage.service
 * <br>file name      : ChatLogService
 * <br>date           : 2024-07-08
 * <pre>
 * <span style="color: white;">[description]</span>
 * 채팅 로그를 작성을 관리하는 Manager class
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 * saveChatMessage(ChatMessage message) : 채팅 로그를 저장
 * formatChatMessage(ChatMessage message) : 채팅 메세지를 로그 형식에 맞게 변환
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
public class ChatLogManager {

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
