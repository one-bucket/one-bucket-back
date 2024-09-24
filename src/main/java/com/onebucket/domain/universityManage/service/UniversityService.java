package com.onebucket.domain.universityManage.service;

import com.onebucket.domain.universityManage.dto.university.DeleteUniversityDto;
import com.onebucket.domain.universityManage.dto.university.UniversityDto;
import com.onebucket.domain.universityManage.dto.university.UpdateUniversityDto;
import com.onebucket.domain.universityManage.dto.verifiedCode.internal.VerifiedCodeCheckDto;
import com.onebucket.domain.universityManage.dto.verifiedCode.internal.VerifiedCodeDto;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.universityManage.service
 * <br>file name      : UniversityService
 * <br>date           : 2024-07-01
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
 * 2024-07-01        SeungHoon              init create
 * </pre>
 */
public interface UniversityService {
    // 대학교 CRUD
    Long createUniversity(UniversityDto universityDto);
    List<UniversityDto> findAllUniversity();
    UniversityDto getUniversity(String name);
    void updateUniversity(UpdateUniversityDto dto);
    void deleteUniversity(DeleteUniversityDto dto);

    // 대학교 이메일 인증
    void verifyCode(VerifiedCodeCheckDto dto);
    String makeVerifiedCode(VerifiedCodeDto dto);
}
