package com.onebucket.domain.universityManage.service;

import com.onebucket.domain.universityManage.dto.verifiedCode.internal.VerifiedCodeCheckDto;
import com.onebucket.domain.universityManage.dto.verifiedCode.internal.VerifiedCodeDto;

/**
 * <br>package name   : com.onebucket.domain.universityManage.service
 * <br>file name      : UniversityEmailVerificationService
 * <br>date           : 2024-09-27
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
 * 2024-09-27        SeungHoon              init create
 * </pre>
 */
public interface UniversityEmailVerificationService {
    void verifyCode(VerifiedCodeCheckDto dto);
    String makeVerifiedCode(VerifiedCodeDto dto);
}
