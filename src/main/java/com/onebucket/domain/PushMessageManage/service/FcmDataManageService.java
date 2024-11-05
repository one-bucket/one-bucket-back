package com.onebucket.domain.PushMessageManage.service;

import com.onebucket.domain.PushMessageManage.dto.MessageLogDto;
import com.onebucket.domain.PushMessageManage.dto.TokenDto;

/**
 * <br>package name   : com.onebucket.domain.PushMessageManage.service
 * <br>file name      : FcmDataManageService
 * <br>date           : 2024-11-05
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
public interface FcmDataManageService {
    void updateDeviceToken(TokenDto.Info dto);
    void saveMessageLog(MessageLogDto.Info dto);
}
