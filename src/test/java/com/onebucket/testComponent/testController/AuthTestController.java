package com.onebucket.testComponent.testController;

import com.onebucket.domain.memberManage.dto.CreateMemberRequestDto;
import com.onebucket.domain.memberManage.dto.SignInRequestDto;
import com.onebucket.domain.memberManage.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <br>package name   : com.onebucket.testController
 * <br>file name      : AuthTestController
 * <br>date           : 2024-06-26
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
 * 2024-06-26        jack8              init create
 * </pre>
 */
@RestController
public class AuthTestController {

    @Autowired
    MemberService memberService;

    @GetMapping("/security-endpoint")
    public String securityEndpoint() {
        return "This is a security endpoint!";
    }

    @GetMapping("/test/url")
    public String testUrl() {
        return "This is a public endpoint";
    }

    @PostMapping("/test/create-testuser")
    public ResponseEntity<String> createTestUser(@RequestBody SignInRequestDto signInDto) {
        CreateMemberRequestDto dto = CreateMemberRequestDto.builder()
                .username(signInDto.getUsername())
                .password(signInDto.getPassword())
                .nickname("test-nick")
                .build();
        try {
            memberService.createMember(dto);
            return ResponseEntity.ok("success");
        } catch(DataIntegrityViolationException ignore) {
            return ResponseEntity.ok("success");
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }


    }
}
