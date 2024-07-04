package com.onebucket.domain.memberManage.api;

import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.memberManage.domain.Profile;
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
 * 해당 컨트롤러는 {@link Member Member} 와 {@link Profile Profile}
 * 에 대한 endpoint 를 제공한다.
 * 1. POST - "/profile/update"
 * 2. POST - "/profile/image
 * 3. POST - "/profile/basicImage"
 * 4. GET - "/profile/image"
 * 5. GET - "/profile"
 * </pre>
 * <pre>
 * modified log :
 * =================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------
 * 2024-07-01        jack8              init create
 * </pre>
 */

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final ProfileService profileService;
    private final SecurityUtils securityUtils;

    /**
     * 사용자의 profile 을 업데이트한다. 이를 위한 dto에는 null 이 존재할 수 있으며 이 경우 해당 필드는 갱신되지 않는다.
     * @param dto 이는 {@link UpdateProfileDto}에 대한 매개변수이며 profile 에 대한 기본적인 정보를 가진다.
     * @return 성공 시 "success update profile" 과 200 code를 반환한다.
     */
    @PostMapping("/profile/update")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileDto dto) {
        String username = securityUtils.getCurrentUsername();
        Long id = memberService.usernameToId(username);
        profileService.updateProfile(id, dto);

        return ResponseEntity.ok("success update profile");
    }

    /**
     * 사용자 profile 의 image 를 갱신한다.
     * @param image MultipartFile 타입의 파일을 http 로부터 받아와 minio 에 저장한다.
     * @return 성공 시 "success update image" 와 200 code 를 반환한다.
     */
    @PostMapping("/profile/image")
    public ResponseEntity<?> updateImage(@RequestParam("file")MultipartFile image) {
        String username = securityUtils.getCurrentUsername();
        Long id = memberService.usernameToId(username);
        profileService.updateImage(id, image);

        return ResponseEntity.ok("success update image");
    }

    /**
     * 사용자가 원할 경우, 자신의 프로필 이미지를 기본 이미지로 바꾼다. 기본 이미지는 이미 저장되어 있다.
     * @return 성공 시 "success update basic image" 와 200 code 를 반환한다.
     */
    @PostMapping("/profile/basicImage")
    public ResponseEntity<?> updateToBasicImage() {
        String username = securityUtils.getCurrentUsername();
        Long id = memberService.usernameToId(username);

        profileService.updateImageToBasic(id);
        return ResponseEntity.ok("success update basic image");
    }

    /**
     * 저장된 이미지를 불러와 반환한다. 반환 타입은 byte[] 이며 http 메시지의 헤더를 MediaType.IMAGE_PNG 로 설정한다.
     * @return byte 로 구성된 이미지와 헤더, 200 code 를 반환한다.
     */
    @GetMapping("/profile/image")
    public ResponseEntity<byte[]> getImage() {
        String username = securityUtils.getCurrentUsername();
        Long id = memberService.usernameToId(username);

        byte[] imageBytes = profileService.readProfileImage(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    /**
     * Get 요청이며, 사용자의 프로필 정보를 불러온다. 단, 이미지의 경우 개별적인 엔드포인트를 통해 가져온다.
     * name, gender, age, description, birth 및 createAt, updateAt을 반환한다.
     * @return ReadProfileDto 를 반환하며 저장된 데이터이다.
     */
    @GetMapping("/profile")
    public ResponseEntity<ReadProfileDto> getProfile() {
        String username = securityUtils.getCurrentUsername();
        Long id = memberService.usernameToId(username);
        return ResponseEntity.ok(profileService.readProfile(id));
    }


}
