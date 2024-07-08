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
 * 해당 컨트롤러는 {@link com.onebucket.domain.memberManage.domain.Member Member} 와 {@link com.onebucket.domain.memberManage.domain.Profile Profile}
 * 에 대한 초기 생성 endpoint 를 제공한다.
 * 1. POST - "/register"
 * </pre>
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-06-27        jack8              init create
 * 2024-07-05        jack8              documentation
 * </pre>
 */

@RestController
@RequiredArgsConstructor
public class RegisterController {

    private final MemberService memberService;
    private final ProfileService profileService;

    /**
     * 기본적으로 입력된 정보를 이용해 Member 엔티티를 생성하고, 이를 기반으로 초기 Profile 엔티티를 생성한다.
     *
     * @param dto {@link CreateMemberRequestDto} 를 입력받아 사용한다.
     * @return "success create" 메시지를 제공한다.
     */
    @PostMapping(path = "/register")
    public ResponseEntity<?> register (@Valid @RequestBody CreateMemberRequestDto dto) {
        Long id = memberService.createMember(dto);
        profileService.createInitProfile(id);
        return ResponseEntity.ok("success create");
    }


}
