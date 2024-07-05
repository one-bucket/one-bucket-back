package com.onebucket.domain.universityManage.service;

import com.onebucket.domain.universityManage.dao.UniversityRepository;
import com.onebucket.domain.universityManage.domain.University;
import com.onebucket.domain.universityManage.dto.ResponseUniversityDto;
import com.onebucket.global.exceptionManage.customException.universityManageException.UniversityException;
import com.onebucket.global.exceptionManage.errorCode.UniversityErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <br>package name   : com.onebucket.domain.universityManage.service
 * <br>file name      : UniversityImpl
 * <br>date           : 2024-07-05
 * <pre>
 * <span style="color: white;">[description]</span>
 *  대학 정보를 출력하는 Service
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
 * 2024-07-05        SeungHoon              init create
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {

    private final UniversityRepository universityRepository;

    /**
     * @return DB에 존재하는 모든 대학 정보 출력
     */
    @Override
    public List<ResponseUniversityDto> findAll() {
        List<University> universities = universityRepository.findAll();
        return universities.stream()
                .map(university -> ResponseUniversityDto.builder()
                        .name(university.getName())
                        .address(university.getAddress())
                        .email(university.getEmail())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * @param name
     * @return 특정 이름을 가진 대학교 정보 출력
     */
    @Override
    public ResponseUniversityDto getUniversityByName(String name) {
        University university = universityRepository.findByName(name)
                .orElseThrow(()-> new UniversityException(UniversityErrorCode.NOT_EXIST_UNIVERSITY));
        return ResponseUniversityDto.builder()
                .name(name)
                .address(university.getAddress())
                .email(university.getEmail())
                .build();
    }
}
