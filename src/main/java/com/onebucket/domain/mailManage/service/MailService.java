package com.onebucket.domain.mailManage.service;

import com.onebucket.domain.mailManage.dto.EmailMessage;
import com.onebucket.domain.universityManage.dto.verifiedCode.internal.VerifiedCodeDto;

import java.util.Map;

/**
 * <br>package name   : com.onebucket.domain.mailManage.service
 * <br>file name      : MailService
 * <br>date           : 2024-09-20
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
 * 2024-09-20        SeungHoon              init create
 * </pre>
 */
public interface MailService {
    void sendEmail(EmailMessage emailMessage, String templateName, Map<String, Object> variables);
}
