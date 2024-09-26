package com.onebucket.domain.universityManage.api;

import com.onebucket.domain.mailManage.dto.EmailMessage;
import com.onebucket.domain.mailManage.service.MailService;
import com.onebucket.domain.universityManage.dto.university.UpdateUniversityDto;
import com.onebucket.domain.universityManage.dto.verifiedCode.internal.VerifiedCodeCheckDto;
import com.onebucket.domain.universityManage.dto.verifiedCode.internal.VerifiedCodeDto;
import com.onebucket.domain.universityManage.dto.verifiedCode.request.RequestCodeCheckDto;
import com.onebucket.domain.universityManage.dto.verifiedCode.request.RequestCodeDto;
import com.onebucket.domain.universityManage.service.UniversityService;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <br>package name   : com.onebucket.domain.universityManage.controller
 * <br>file name      : UniversityController
 * <br>date           : 2024-07-05
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
 * 2024-07-05        SeungHoon              init create
 * </pre>
 */
@RestController
@RequiredArgsConstructor
public class UniversityController {

    private final UniversityService universityService;
    private final MailService mailService;
    private final SecurityUtils securityUtils;

    @PostMapping("/univ/send-code")
    public ResponseEntity<SuccessResponseDto> sendVerifiedCode(@RequestBody RequestCodeDto dto) {
        String username = securityUtils.getCurrentUsername();
        VerifiedCodeDto verifiedCodeDto = VerifiedCodeDto.of(dto,username);
        String verifiedCode = universityService.makeVerifiedCode(verifiedCodeDto);
        EmailMessage emailMessage = EmailMessage.of(dto.universityEmail(),"[한바구니] 학교 이메일 인증");
        // 템플릿에 전달할 데이터를 설정
        Map<String, Object> variables = new HashMap<>();
        variables.put("verificationCode", verifiedCode);
        mailService.sendEmail(emailMessage, "email-verification", variables);
        return ResponseEntity.ok(new SuccessResponseDto("success send verifiedCode"));
    }

    @PostMapping("/univ/verify-code")
    public ResponseEntity<SuccessResponseDto> verifyCode(@RequestBody RequestCodeCheckDto dto) {
        String username = securityUtils.getCurrentUsername();
        VerifiedCodeCheckDto verifiedCodeCheckDto = VerifiedCodeCheckDto.of(username,dto);
        universityService.verifyCode(verifiedCodeCheckDto);
        return ResponseEntity.ok(new SuccessResponseDto("verify code success"));
    }
}
