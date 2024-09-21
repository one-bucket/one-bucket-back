package com.onebucket.domain.universityManage.api;

import com.onebucket.domain.mailManage.dto.EmailMessage;
import com.onebucket.domain.mailManage.service.MailService;
import com.onebucket.domain.universityManage.dto.university.UniversityDto;
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

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.universityManage.controller
 * <br>file name      : UniversityController
 * <br>date           : 2024-07-05
 * <pre>
 * <span style="color: white;">[description]</span>
 * POST - "/admin/univ/create"
 * GET - "/admin/univs"
 * GET - "/admin/univ/{id}
 * DELETE - "/admin/univ/{id}
 * ---------------------------------------
 * PUT - "/admin/univ/{id}/email"
 * PUT - "/admin/univ/{id}/address"
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
@RequestMapping("/admin")
public class UniversityController {

    private final UniversityService universityService;
    private final MailService mailService;
    private final SecurityUtils securityUtils;

    @PostMapping("/univs")
    public ResponseEntity<SuccessResponseDto> createUniversity(@Valid @RequestBody UniversityDto universityDto) {
        Long id = universityService.createUniversity(universityDto);
        return ResponseEntity.ok(new SuccessResponseDto("success create university / id is " + id));
    }

    @GetMapping("/univs")
    public ResponseEntity<?> getAllUniversity() {
        List<UniversityDto> universities = universityService.findAllUniversity();
        return ResponseEntity.ok(universities);
    }

    @GetMapping("/univs/{name}")
    public ResponseEntity<?> getUniversity(@PathVariable String name) {
        UniversityDto universityDto = universityService.getUniversity(name);
        return ResponseEntity.ok(universityDto);
    }

    @PatchMapping("/univs/{name}")
    public ResponseEntity<SuccessResponseDto> updateUniversity(@PathVariable String name,
                                                                          @Valid @RequestBody UpdateUniversityDto dto) {
        universityService.updateUniversity(name,dto);
        return ResponseEntity.ok(new SuccessResponseDto("success update university"));
    }

    @DeleteMapping("/univs/{name}")
    public ResponseEntity<SuccessResponseDto> deleteUniversity(@PathVariable String name) {
        universityService.deleteUniversity(name);
        return ResponseEntity.ok(new SuccessResponseDto("success delete university"));
    }

    @PostMapping("/univs/send-code")
    public ResponseEntity<SuccessResponseDto> sendVerifiedCode(@RequestBody RequestCodeDto dto) {
        String username = securityUtils.getCurrentUsername();
        VerifiedCodeDto verifiedCodeDto = VerifiedCodeDto.of(dto,username);
        String verifiedCode = universityService.makeVerifiedCode(verifiedCodeDto);
        EmailMessage emailMessage = EmailMessage.of(dto.universityEmail(),"[한바구니] 학교 이메일 인증");
        mailService.sendEmail(emailMessage,verifiedCode);
        return ResponseEntity.ok(new SuccessResponseDto("success send verifiedCode"));
    }

    @PostMapping("/univs/verify-code")
    public ResponseEntity<SuccessResponseDto> verifyCode(@RequestBody RequestCodeCheckDto dto) {
        String username = securityUtils.getCurrentUsername();
        VerifiedCodeCheckDto verifiedCodeCheckDto = VerifiedCodeCheckDto.of(username,dto);
        universityService.verifyCode(verifiedCodeCheckDto);
        return ResponseEntity.ok(new SuccessResponseDto("verify code success"));
    }
}
