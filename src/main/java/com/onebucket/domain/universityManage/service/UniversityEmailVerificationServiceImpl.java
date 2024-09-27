package com.onebucket.domain.universityManage.service;

import com.onebucket.domain.memberManage.dao.ProfileRepository;
import com.onebucket.domain.universityManage.dao.UniversityRepository;
import com.onebucket.domain.universityManage.domain.University;
import com.onebucket.domain.universityManage.dto.verifiedCode.internal.VerifiedCodeCheckDto;
import com.onebucket.domain.universityManage.dto.verifiedCode.internal.VerifiedCodeDto;
import com.onebucket.global.exceptionManage.customException.universityManageException.UniversityException;
import com.onebucket.global.exceptionManage.customException.verificationException.VerificationException;
import com.onebucket.global.exceptionManage.errorCode.UniversityErrorCode;
import com.onebucket.global.exceptionManage.errorCode.VerificationErrorCode;
import com.onebucket.global.redis.RedisRepository;
import com.onebucket.global.utils.RandomStringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <br>package name   : com.onebucket.domain.universityManage.service
 * <br>file name      : UniversityEmailVerificationService
 * <br>date           : 2024-09-26
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
 * 2024-09-26        SeungHoon              init create
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class UniversityEmailVerificationServiceImpl implements UniversityEmailVerificationService {

    private final UniversityRepository universityRepository;
    private final RandomStringUtils randomStringUtils;
    private final RedisRepository redisRepository;
    private final ProfileRepository profileRepository;

    private static final long EXPIRED_TIME = 5L;

    /**
     * 전달 받은 인증코드가 유효하면 프로필에 학교 이메일 추가
     * @param dto
     */
    @Override
    public void verifyCode(VerifiedCodeCheckDto dto) {
        String storedCode = redisRepository.get(createVerificationCodeKey(dto.username(),dto.universityEmail()));
        // 인증 코드 검증
        if (storedCode == null || !storedCode.equals(dto.verifiedCode())) {
            throw new VerificationException(VerificationErrorCode.INVALID_VERIFICATION_CODE);
        }
    }

    /**
     * 유효한 이메일이라면 인증코드 생성
     * @param dto
     * @return
     */
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

        // 이미 db에 저장되어 있는 이메일인지 확인
        if(profileRepository.existsByEmail(dto.universityEmail())) {
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
