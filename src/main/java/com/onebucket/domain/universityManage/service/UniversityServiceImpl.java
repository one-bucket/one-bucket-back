package com.onebucket.domain.universityManage.service;

import com.onebucket.domain.universityManage.dao.UniversityRepository;
import com.onebucket.domain.universityManage.domain.University;
import com.onebucket.domain.universityManage.dto.CreateUniversityDto;
import com.onebucket.domain.universityManage.dto.ResponseUniversityDto;
import com.onebucket.domain.universityManage.dto.UpdateUniversityDto;
import com.onebucket.global.exceptionManage.customException.universityManageException.UniversityException;
import com.onebucket.global.exceptionManage.errorCode.UniversityErrorCode;
import com.onebucket.global.utils.EntityUtils;
import com.onebucket.global.utils.UniversityEmailValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final UniversityEmailValidator universityEmailValidator;

    /**
     * 새로운 대학 정보를 만들고 만든 대학 정보를 반환한다. 같은 이름을 가진 대학교는 추가할 수 없음.
     * @param createUniversityDto
     * @return University의 id
     */
    @Override
    @Transactional
    public Long createUniversity(CreateUniversityDto createUniversityDto) {
        String name = createUniversityDto.getName();
        String email = createUniversityDto.getEmail();

        // 대학교 이메일 유효성 검사
        if (!universityEmailValidator.isValidUniversityEmail(name, email)) {
            throw new UniversityException(UniversityErrorCode.INVALID_EMAIL, "Invalid university email");
        }

        University university = University.builder()
                .name(createUniversityDto.getName())
                .address(createUniversityDto.getAddress())
                .email(createUniversityDto.getEmail())
                .build();
        try {
            universityRepository.save(university);
            return university.getId();
        } catch (DataIntegrityViolationException e) {
            throw new UniversityException(UniversityErrorCode.DUPLICATE_UNIVERSITY,
                    "University already exist");
        }
    }

    /**
     * @return DB에 존재하는 모든 대학 정보 출력
     */
    @Override
    public List<ResponseUniversityDto> findAllUniversity() {
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
     * @param id
     * @return 특정 이름을 가진 대학교 정보 출력
     */
    @Override
    public ResponseUniversityDto getUniversity(Long id) {
        University university = universityRepository.findById(id)
                .orElseThrow(()-> new UniversityException(UniversityErrorCode.NOT_EXIST_UNIVERSITY));
        return ResponseUniversityDto.builder()
                .name(university.getName())
                .address(university.getAddress())
                .email(university.getEmail())
                .build();
    }

    /**
     * 대학 정보 업데이트하기.
     * @param id
     * @param dto
     */
    @Override
    @Transactional
    public void updateUniversity(Long id, UpdateUniversityDto dto) {
        University university = universityRepository.findById(id)
                .orElseThrow(()->new UniversityException(UniversityErrorCode.NOT_EXIST_UNIVERSITY));

        EntityUtils.updateIfNotNull(dto.getAddress(),university::setAddress);
        EntityUtils.updateIfNotNull(dto.getEmail(),university::setEmail);
    }

    /**
     * 대학 정보 삭제하기
     * @param id
     */
    @Override
    public void deleteUniversity(Long id) {
        University university = universityRepository.findById(id)
                .orElseThrow(()->new UniversityException(UniversityErrorCode.NOT_EXIST_UNIVERSITY));
        universityRepository.delete(university);
    }
}
