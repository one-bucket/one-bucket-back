package com.onebucket.domain.universityManage.service;

import com.onebucket.domain.memberManage.dao.ProfileRepository;
import com.onebucket.domain.universityManage.dao.UniversityRepository;
import com.onebucket.domain.universityManage.domain.University;
import com.onebucket.domain.universityManage.dto.university.DeleteUniversityDto;
import com.onebucket.domain.universityManage.dto.university.ResponseUniversityDto;
import com.onebucket.domain.universityManage.dto.university.UpdateUniversityDto;
import com.onebucket.domain.universityManage.dto.verifiedCode.internal.VerifiedCodeCheckDto;
import com.onebucket.domain.universityManage.dto.verifiedCode.internal.VerifiedCodeDto;
import com.onebucket.global.exceptionManage.customException.universityManageException.UniversityException;
import com.onebucket.global.exceptionManage.customException.verificationException.VerificationException;
import com.onebucket.global.exceptionManage.errorCode.UniversityErrorCode;
import com.onebucket.global.exceptionManage.errorCode.ValidateErrorCode;
import com.onebucket.global.exceptionManage.errorCode.VerificationErrorCode;
import com.onebucket.global.redis.RedisRepository;
import com.onebucket.global.utils.EntityUtils;
import com.onebucket.global.utils.RandomStringUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
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
    private final RandomStringUtils randomStringUtils;
    private final RedisRepository redisRepository;
    private final ProfileRepository profileRepository;

    private static final long EXPIRED_TIME = 5L;
    /**
     * 새로운 대학 정보를 만들고 만든 대학 정보를 반환한다. 같은 이름을 가진 대학교는 추가할 수 없음.
     * @param responseUniversityDto 생성하고자 하는 대학의 정보를 담는다.
     * @return University 의 id
     */
    @Override
    @Transactional
    public Long createUniversity(ResponseUniversityDto responseUniversityDto) {

        University university = University.builder()
                .name(responseUniversityDto.getName())
                .address(responseUniversityDto.getAddress())
                .email(responseUniversityDto.getEmail())
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
        if (universities.isEmpty()) {
            ResponseUniversityDto defaultDto = ResponseUniversityDto.builder()
                    .name("not insert")
                    .address("data")
                    .email("yet")
                    .build();
            return List.of(defaultDto);
        }
        return universities.stream()
                .map(university -> ResponseUniversityDto.builder()
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
    public ResponseUniversityDto getUniversity(String name) {
        University university = universityRepository.findByName(name)
                .orElseThrow(()-> new UniversityException(UniversityErrorCode.NOT_EXIST_UNIVERSITY));
        return ResponseUniversityDto.builder()
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

    /**
     * 전달 받은 인증코드가 유효하면 프로필에 학교 이메일 추가
     * @param dto
     */
    @Override
    public void verifyCode(VerifiedCodeCheckDto dto) {
        String storedCode = redisRepository.get(dto.username());
        // 인증 코드 검증
        if (storedCode == null || !storedCode.equals(dto.verifiedCode())) {
            throw new VerificationException(VerificationErrorCode.INVALID_VERIFICATION_CODE);
        }
    }

    @Override
    public String makeVerifiedCode(VerifiedCodeDto dto) {
        University university = universityRepository.findByName(dto.university())
                .orElseThrow(() -> new UniversityException(UniversityErrorCode.NOT_EXIST_UNIVERSITY));

        // 유효한 이메일인지 확인
        validateUniversityEmail(dto, university);

        // 인증 코드 생성
        String verifiedCode = randomStringUtils.generateRandomStr(6);
        redisRepository.save()
                        .key(createVerificationCodeKey(dto.username(), dto.universityEmail()))
                        .value(verifiedCode)
                        .timeout(EXPIRED_TIME)
                        .timeUnit(TimeUnit.MINUTES)
                        .save();
        return verifiedCode;
    }

    private void validateUniversityEmail(VerifiedCodeDto dto, University university) {
        // 이메일 형식이 맞는지 확인
        if(!dto.universityEmail().endsWith(university.getEmail())) {
            throw new VerificationException(VerificationErrorCode.INVALID_EMAIL);
        }

        // redis 에 관련 정보가 저장되어 있는지, 다른 이용자가 동일한 이메일로 인증하고 있는 중인지 체크
        String emailOwnershipKey = createEmailOwnershipKey(dto.universityEmail());
        String currentOwner = redisRepository.get(emailOwnershipKey);

        if(currentOwner == null) {
            // 현 이용자가 이메일 인증을 하고 있음을 저장한다.
            redisRepository.save()
                    .key(emailOwnershipKey)
                    .value(dto.username())
                    .timeout(EXPIRED_TIME)
                    .timeUnit(TimeUnit.MINUTES)
                    .save();
        } else if (!currentOwner.equals(dto.username())) {
            // 이미 동일한 email로 다른 사용자가 인증을 시도하고 있음.
            throw new VerificationException(VerificationErrorCode.DUPLICATE_EMAIL);
        }
        String username = dto.username();
        String universityEmail = dto.universityEmail();

        // 이미 db에 저장되어 있는 이메일인지 확인
        if(profileRepository.existsByEmail(universityEmail)) {
            throw new VerificationException(VerificationErrorCode.DUPLICATE_EMAIL);
        }
    }

    private String createVerificationCodeKey(String username, String universityEmail) {
        return username + ":" + universityEmail;
    }
    private String createEmailOwnershipKey(String universityEmail) {
        return "emailOwnership:" + universityEmail;
    }
}
