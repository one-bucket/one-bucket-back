package com.onebucket.domain.universityManage.service;

import com.onebucket.domain.universityManage.domain.University;
import com.onebucket.domain.universityManage.dto.CreateUniversityDto;
import com.onebucket.domain.universityManage.dto.ResponseUniversityDto;
import com.onebucket.domain.universityManage.dto.UpdateUniversityAddressDto;
import com.onebucket.domain.universityManage.dto.UpdateUniversityEmailDto;

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
    Long createUniversity(CreateUniversityDto createUniversityDto);
    List<ResponseUniversityDto> findAllUniversity();
    ResponseUniversityDto getUniversity(Long id);
    void updateUniversityAddress(Long id, UpdateUniversityAddressDto updateUniversityAddressDto);
    void updateUniversityEmail(Long id, UpdateUniversityEmailDto updateUniversityEmailDto);
    void deleteUniversity(Long id);
}
