package com.onebucket.global.exceptionManage.customException.chatManageException.Exceptions;

import com.onebucket.global.exceptionManage.customException.chatManageException.ChatManageException;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import com.onebucket.global.exceptionManage.errorCode.ErrorCode;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.customException.chatManageException.Exceptions
 * <br>file name      : ChatRoomFullException
 * <br>date           : 2024-08-12
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
 * 2024-08-12        SeungHoon              init create
 * </pre>
 */
public class ChatRoomFullException extends ChatManageException {
    public ChatRoomFullException(ChatErrorCode chatErrorCode) {super(chatErrorCode);}

    public ChatRoomFullException(ErrorCode errorCode, String message) {super(errorCode,message);}
}