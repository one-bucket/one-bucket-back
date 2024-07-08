package com.onebucket.global.exceptionManage.exceptionHandler;

import com.onebucket.global.exceptionManage.ErrorResponse;
import com.onebucket.global.exceptionManage.customException.chatManageException.ChatManageException;
import com.onebucket.global.exceptionManage.customException.chatManageException.TopicNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.exceptionHandler
 * <br>file name      : ChatExceptionHandler
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
@RestControllerAdvice
@Slf4j
public class ChatExceptionHandler {
    @ExceptionHandler(TopicNotFoundException.class)
    public ResponseEntity<?> handleChatException(ChatManageException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, ex.getErrorCode().getHttpStatus());
    }
}
