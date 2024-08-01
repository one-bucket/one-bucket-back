package com.onebucket.domain.universityManage.service;

import com.onebucket.domain.universityManage.dto.UniversityDto;
import com.onebucket.domain.universityManage.dto.UpdateUniversityDto;

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
    Long createUniversity(UniversityDto universityDto);
    List<UniversityDto> findAllUniversity();
    UniversityDto getUniversity(String name);
    void updateUniversity(String name, UpdateUniversityDto dto);
    void deleteUniversity(String name);
}
