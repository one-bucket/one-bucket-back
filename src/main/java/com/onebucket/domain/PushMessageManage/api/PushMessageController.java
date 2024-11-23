package com.onebucket.domain.PushMessageManage.api;

import com.onebucket.domain.PushMessageManage.dto.TokenDto;
import com.onebucket.domain.PushMessageManage.service.FcmDataManageService;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <br>package name   : com.onebucket.domain.PushMessageManage.api
 * <br>file name      : PushMessageController
 * <br>date           : 11/22/24
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 */
@RestController
@RequiredArgsConstructor
public class PushMessageController {
    private final FcmDataManageService fcmDataManageService;
    private final SecurityUtils securityUtils;

    @PostMapping("/device-token/register")
    public ResponseEntity<?> registerDeviceToken(@RequestBody TokenDto.Token dto) {
        Long userId = securityUtils.getUserId();
        TokenDto.Info tokenInfo = TokenDto.Info.builder()
                .userId(userId)
                .token(dto.getToken())
                .build();
        fcmDataManageService.updateDeviceToken(tokenInfo);

        return ResponseEntity.ok(new SuccessResponseDto("success register device token"));
    }

}
