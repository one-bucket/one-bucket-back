package com.onebucket.domain.universityManage.service;

import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.universityManage.dao.UniversityRepository;
import com.onebucket.domain.universityManage.domain.University;
import com.onebucket.domain.universityManage.dto.university.DeleteUniversityDto;
import com.onebucket.domain.universityManage.dto.university.UniversityDto;
import com.onebucket.domain.universityManage.dto.university.UpdateUniversityDto;
import com.onebucket.domain.universityManage.dto.verifiedCode.internal.VerifiedCodeCheckDto;
import com.onebucket.domain.universityManage.dto.verifiedCode.internal.VerifiedCodeDto;
import com.onebucket.global.exceptionManage.customException.universityManageException.UniversityException;
import com.onebucket.global.exceptionManage.errorCode.UniversityErrorCode;
import com.onebucket.global.redis.RedisRepository;
import com.onebucket.global.utils.EntityUtils;
import com.onebucket.global.utils.RandomStringUtils;
import com.onebucket.global.utils.UniversityEmailValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
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
@Slf4j
public class UniversityServiceImpl implements UniversityService {

    private final UniversityRepository universityRepository;
    private final UniversityEmailValidator validator;
    private final RandomStringUtils randomStringUtils;
    private final RedisRepository redisRepository;

    /**
     * 새로운 대학 정보를 만들고 만든 대학 정보를 반환한다. 같은 이름을 가진 대학교는 추가할 수 없음.
     * @param universityDto 생성하고자 하는 대학의 정보를 담는다.
     * @return University 의 id
     */
    @Override
    @Transactional
    public Long createUniversity(UniversityDto universityDto) {

        University university = University.builder()
                .name(universityDto.getName())
                .address(universityDto.getAddress())
                .email(universityDto.getEmail())
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
    public List<UniversityDto> findAllUniversity() {
        List<University> universities = universityRepository.findAll();
        if (universities.isEmpty()) {
            UniversityDto defaultDto = UniversityDto.builder()
                    .name("not insert")
                    .address("data")
                    .email("yet")
                    .build();
            return List.of(defaultDto);
        }
        return universities.stream()
                .map(university -> UniversityDto.builder()
                        .name(university.getName())
                        .address(university.getAddress())
                        .email(university.getEmail())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * @param name 대학의 Name 을 입력
     * @return 특정 이름을 가진 대학교 정보 출력
     */
    @Override
    public UniversityDto getUniversity(String name) {
        University university = universityRepository.findByName(name)
                .orElseThrow(()-> new UniversityException(UniversityErrorCode.NOT_EXIST_UNIVERSITY));
        return UniversityDto.builder()
                .name(university.getName())
                .address(university.getAddress())
                .email(university.getEmail())
                .build();
    }

    @Override
    @Transactional
    public void updateUniversity(UpdateUniversityDto dto) {
        University university = universityRepository.findByName(dto.getName())
                .orElseThrow(()->new UniversityException(UniversityErrorCode.NOT_EXIST_UNIVERSITY));

        EntityUtils.updateIfNotNull(dto.getAddress(),university::setAddress);
        EntityUtils.updateIfNotNull(dto.getEmail(),university::setEmail);
    }

    @Override
    public void deleteUniversity(DeleteUniversityDto dto) {
        University university = universityRepository.findByName(dto.name())
                .orElseThrow(()->new UniversityException(UniversityErrorCode.NOT_EXIST_UNIVERSITY));
        universityRepository.delete(university);
    }

    @Override
    public void verifyCode(VerifiedCodeCheckDto dto) {
        String storedCode = redisRepository.get(dto.universityEmail());
        // 인증 코드 검증
        if (storedCode == null || !storedCode.equals(dto.verifiedCode())) {
            throw new UniversityException(UniversityErrorCode.INVALID_VERIFICATION_CODE);
        }
    }

    @Override
    public String makeVerifiedCode(VerifiedCodeDto dto) {
        // 학교 인증에 대한 로직을 다시 작성해야한다.
        if(!validator.isValidUniversityEmail(dto)) {
            throw new UniversityException(UniversityErrorCode.INVALID_EMAIL);
        }
        // 인증 코드 생성
        String verifiedCode = randomStringUtils.generateRandomStr(6);
        long EXPIRED_TIME = 5L;
        redisRepository.save()
                        .key(dto.universityEmail())
                        .value(verifiedCode)
                        .timeout(EXPIRED_TIME)
                        .timeUnit(TimeUnit.MINUTES)
                        .save();
        return verifiedCode;
    }
}
