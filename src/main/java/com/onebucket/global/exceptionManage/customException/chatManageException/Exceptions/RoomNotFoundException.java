package com.onebucket.global.exceptionManage.customException.chatManageException.Exceptions;

import com.onebucket.global.exceptionManage.customException.chatManageException.ChatManageException;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import com.onebucket.global.exceptionManage.errorCode.ErrorCode;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.customException.chatManageException
 * <br>file name      : RoomNotFoundException
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
public class RoomNotFoundException extends ChatManageException {
    public RoomNotFoundException(ChatErrorCode chatErrorCode) {super(chatErrorCode);}

    public RoomNotFoundException(ErrorCode errorCode,String message) {super(errorCode,message);}
}
