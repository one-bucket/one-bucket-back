package com.onebucket.domain.memberManage.api;

import com.onebucket.domain.memberManage.dto.ReadProfileDto;
import com.onebucket.domain.memberManage.dto.UpdateProfileDto;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.memberManage.service.ProfileService;
import com.onebucket.global.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <br>package name   : com.onebucket.domain.memberManage.api
 * <br>file name      : MemberController
 * <br>date           : 2024-07-01
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
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-07-01        jack8              init create
 * </pre>
 */

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final ProfileService profileService;
    private final SecurityUtils securityUtils;

    @PostMapping("/profile/update")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileDto dto) {
        String username = securityUtils.getCurrentUsername();
        Long id = memberService.usernameToId(username);
        profileService.updateProfile(id, dto);

        return ResponseEntity.ok("success update profile");
    }

    @PostMapping("/profile/image")
    public ResponseEntity<?> updateImage(@RequestParam("file")MultipartFile image) {
        String username = securityUtils.getCurrentUsername();
        Long id = memberService.usernameToId(username);
        profileService.updateImage(id, image);

        return ResponseEntity.ok("success update image");
    }

    @PostMapping("/profile/basicImage")
    public ResponseEntity<?> updateToBasicImage() {
        String username = securityUtils.getCurrentUsername();
        Long id = memberService.usernameToId(username);

        profileService.updateImageToBasic(id);
        return ResponseEntity.ok("success update basic image");
    }

    @GetMapping("/profile/image")
    public ResponseEntity<byte[]> getImage() {
        String username = securityUtils.getCurrentUsername();
        Long id = memberService.usernameToId(username);

        byte[] imageBytes = profileService.readProfileImage(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<ReadProfileDto> getProfile() {
        String username = securityUtils.getCurrentUsername();
        Long id = memberService.usernameToId(username);
        return ResponseEntity.ok(profileService.readProfile(id));
    }


}
