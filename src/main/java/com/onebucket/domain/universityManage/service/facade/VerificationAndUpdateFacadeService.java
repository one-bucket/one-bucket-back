package com.onebucket.domain.universityManage.service.facade;

import com.onebucket.domain.memberManage.dto.internal.SetUniversityDto;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.memberManage.service.ProfileService;
import com.onebucket.domain.universityManage.dto.verifiedCode.internal.VerifiedCodeCheckDto;
import com.onebucket.domain.universityManage.dto.verifiedCode.request.RequestCodeCheckDto;
import com.onebucket.domain.universityManage.service.UniversityEmailVerificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <br>package name   : com.onebucket.global.auth.verification.service
 * <br>file name      : VerificationService
 * <br>date           : 2024-09-26
 * <pre>
 * <span style="color: white;">[description]</span>
 * universityController 에서 사용하며 인증코드가 유효하면 member,profile의 대학교, 이메일 필드를 채워준다.
 * 순환참조가 일어나지 않도록 해야 한다.
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
public class VerificationAndUpdateFacadeService {

    private final UniversityEmailVerificationServiceImpl universityEmailVerificationServiceImpl;
    private final ProfileService profileService;
    private final MemberService memberService;

    public void verifyAndUpdateProfileAndMember(String username, RequestCodeCheckDto dto) {
        // 인증 코드 검증
        VerifiedCodeCheckDto verifiedCodeCheckDto = VerifiedCodeCheckDto.of(username, dto);
        universityEmailVerificationServiceImpl.verifyCode(verifiedCodeCheckDto);

        // 프로필 이메일 업데이트
        profileService.updateProfileEmail(username, dto.universityEmail());

        // 멤버 정보 업데이트
        SetUniversityDto setUniversityDto = SetUniversityDto.builder()
                .username(username)
                .university(dto.university())
                .build();
        memberService.setUniversity(setUniversityDto);
    }
}
