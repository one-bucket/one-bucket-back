package com.onebucket.domain.memberManage.api;

import com.onebucket.domain.memberManage.dto.CreateMemberRequestDto;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.memberManage.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <br>package name   : com.onebucket.domain.memberManage.api
 * <br>file name      : RegisterController
 * <br>date           : 2024-06-27
 * <pre>
 * <span style="color: white;">[description]</span>
 * 1. post - "/register" by {@link CreateMemberRequestDto} / Return "success create"
 * </pre>
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-06-27        jack8              init create
 * </pre>
 */

@RestController
@RequiredArgsConstructor
public class RegisterController {

    private final MemberService memberService;
    private final ProfileService profileService;

    @PostMapping(path = "/register")
    public ResponseEntity<?> register (@Valid @RequestBody CreateMemberRequestDto dto) {
        Long id = memberService.createMember(dto);
        profileService.createInitProfile(id);
        return ResponseEntity.ok("success create");
    }


}
