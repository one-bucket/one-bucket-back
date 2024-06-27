package com.onebucket.domain.memberManage.api;

import com.onebucket.domain.memberManage.dto.SignInRequestDto;
import com.onebucket.domain.memberManage.service.SignInService;
import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * <br>package name   : com.onebucket.domain.memberManage.api
 * <br>file name      : SignInController
 * <br>date           : 2024-06-27
 * <pre>
 * <span style="color: white;">[description]</span>
 * 1. post - "/sign-in" by {@link SignInRequestDto} / Return {@link JwtToken}
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
public class SignInController {
    private final SignInService signInService;

    @PostMapping(path = "/sign-in")
    public ResponseEntity<JwtToken> signIn (@Valid @RequestBody SignInRequestDto dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();

        JwtToken jwtToken = signInService.signInByUsernameAndPassword(username, password);
        return ResponseEntity.ok(jwtToken);
    }

}
