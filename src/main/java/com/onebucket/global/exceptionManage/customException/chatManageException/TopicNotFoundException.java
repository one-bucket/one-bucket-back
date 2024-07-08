package com.onebucket.global.exceptionManage.customException.chatManageException;

import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import com.onebucket.global.exceptionManage.errorCode.ErrorCode;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.customException.chatManageException
 * <br>file name      : TopicNotFoundException
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
public class TopicNotFoundException extends ChatManageException {
    public TopicNotFoundException(ChatErrorCode chatErrorCode) {super(chatErrorCode);}
    public TopicNotFoundException(ErrorCode errorCode,String message) {super(errorCode,message);}
}
