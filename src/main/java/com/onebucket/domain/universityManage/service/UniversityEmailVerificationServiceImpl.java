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

import java.util.Set;
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
    private static final String HEADER = "verify:";

    /**
     * 전달 받은 인증코드가 유효하면 프로필에 학교 이메일 추가
     * @param dto
     */
    @Override
    public void verifyCode(VerifiedCodeCheckDto dto) {
        String storedCode = redisRepository.get(createVerificationCodeKey(dto.universityEmail(),dto.username()));
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
                .key(createVerificationCodeKey(dto.universityEmail(), dto.username()))
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

        // 이미 인증 중인 이메일인가?
        validateEmailOwnership(dto.universityEmail(),dto.username());

        // 이미 db에 저장되어 있는 이메일인지 확인
        if(profileRepository.existsByEmail(dto.universityEmail())) {
            throw new VerificationException(VerificationErrorCode.DUPLICATE_EMAIL);
        }
    }

    private String createVerificationCodeKey(String universityEmail, String username) {
        return HEADER + universityEmail + ":" + username;
    }

    private void validateEmailOwnership(String universityEmail, String username) {
        Set<String> keys = redisRepository.keys(HEADER + universityEmail + ":*");

        if(keys != null && !keys.isEmpty()) {
            for(String key : keys) {
                int lastColonIndex = key.lastIndexOf(":");
                if(lastColonIndex != -1) {
                    String existUsername = key.substring(lastColonIndex + 1);
                    if(!username.equals(existUsername)) {
                        throw new VerificationException(VerificationErrorCode.DUPLICATE_EMAIL);
                    }
                }
            }
        }
    }
}
