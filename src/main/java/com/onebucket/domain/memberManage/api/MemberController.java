package com.onebucket.domain.memberManage.api;

import com.onebucket.domain.mailManage.dto.EmailMessage;
import com.onebucket.domain.mailManage.service.MailService;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.memberManage.domain.Profile;
import com.onebucket.domain.memberManage.dto.*;
import com.onebucket.domain.memberManage.dto.RequestSetEmailDto.RequestSetEmailDto;
import com.onebucket.domain.memberManage.dto.internal.SetUniversityDto;
import com.onebucket.domain.memberManage.dto.request.RequestSetUnivDto;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.memberManage.service.ProfileService;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

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
 *
 * @tested yes
 */

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final ProfileService profileService;
    private final SecurityUtils securityUtils;
    private final MailService mailService;
    /**
     * 사용자가 비밀번호를 잊었을 때, 임시 비밀번호를 설정한다.
     * TODO: 권한 문제
     * @return 200 code
     * @tested yes
     */
    @PostMapping("/member/password/reset")
    public ResponseEntity<SuccessResponseDto> resetPassword() {
        // 비밀 번호 변경하기
        String username = securityUtils.getCurrentUsername();
        String temporaryPassword = memberService.changePassword(username);

        // 인증번호 보내기
        Long id = memberService.usernameToId(username);
        ReadProfileDto profile = profileService.readProfile(id);
        EmailMessage emailMessage = EmailMessage.of(profile.getEmail(),"[한바구니] 임시 비밀번호 발급");
        Map<String, Object> variables = new HashMap<>();
        variables.put("temporaryPassword", temporaryPassword);
        mailService.sendEmail(emailMessage,"reset-password",variables);
        return ResponseEntity.ok(new SuccessResponseDto("success reset password"));
    }

    /**
     * 로그인이 되어 있는 상태에서 비밀번호를 변경할 수 있도록 한다.
     * @param dto 비밀번호가 포함되어 있다.
     * @return 200 code
     * @tested yes
     */
    @PostMapping("/member/password/set")
    public ResponseEntity<SuccessResponseDto> setPassword(@Valid @RequestBody SetPasswordDto dto) {
        String username = securityUtils.getCurrentUsername();
        String password = dto.getPassword();
        memberService.changePassword(username, password);
        return ResponseEntity.ok(new SuccessResponseDto("success set password"));
    }

    /**
     * 로그인이 되어 있는 상태에서 자신의 nickname 을 바꾼다.
     * @param dto nickname 이 포함되어 있다.
     * @return 200 return
     * @tested yes
     */
    @PostMapping("/member/nickname/set")
    public ResponseEntity<SuccessResponseDto> setNickname(@Valid @RequestBody NicknameRequestDto dto) {
        String username = securityUtils.getCurrentUsername();
        memberService.updateMember(username, dto);
        return ResponseEntity.ok(new SuccessResponseDto("success set nickname"));
    }

    /**
     * id를 입력받아 nickname 을 출력한다. id는 url 에 포함되어 있으며 이를 통해 닉네임을 불러온다.
     * @param id url 에 포함되어 있다.
     * @return nickname 에 대한 dto
     * @tested yes
     */
    @GetMapping("/member/{id}/nickname")
    public ResponseEntity<NicknameRequestDto> getNickname(@PathVariable("id") Long id) {
        String nickname = memberService.idToNickname(id);
        NicknameRequestDto result = new NicknameRequestDto(nickname);
        return ResponseEntity.ok(result);
    }

    /**
     * 로그인한 상태에서 해당 유저의 닉네임과 username 을 반환한다.
     * @return username, nickname
     * @tested yes
     */
    @GetMapping("/member/info")
    public ResponseEntity<ReadMemberInfoDto> getMemberInfo() {
        String username = securityUtils.getCurrentUsername();
       return ResponseEntity.ok(memberService.readMember(username));
    }

    /**
     * 로그인한 상태에서 자신의 계정을 탈퇴한다.
     * @return "message" : "success delete account"
     * @tested yes
     */
    @DeleteMapping("/member")
    public ResponseEntity<SuccessResponseDto> quitAccount() {
        String username = securityUtils.getCurrentUsername();
        memberService.quitMember(username);
        return ResponseEntity.ok(new SuccessResponseDto("success delete account"));
    }


    /**
     * 사용자의 profile 을 업데이트한다. 이를 위한 dto dtp 에는 null 이 존재할 수 있으며 이 경우 해당 필드는 갱신되지 않는다.
     * @param dto 이는 {@link UpdateProfileDto}에 대한 매개변수이며 profile 에 대한 기본적인 정보를 가진다.
     * @return 성공 시 "success update profile" 과 200 code 를 반환한다.
     * @tested yes
     */
    @PostMapping("/profile/update")
    public ResponseEntity<SuccessResponseDto> updateProfile(@Valid @RequestBody UpdateProfileDto dto) {
        String username = securityUtils.getCurrentUsername();
        Long id = memberService.usernameToId(username);
        profileService.updateProfile(id, dto);

        return ResponseEntity.ok(new SuccessResponseDto("success update profile"));
    }

    /**
     * 사용자 profile 의 image 를 갱신한다.
     * @param image MultipartFile 타입의 파일을 http 로부터 받아와 minio 에 저장한다.
     * @return 성공 시 "success update image" 와 200 code 를 반환한다.
     * @tested yes
     */
    @PostMapping("/profile/image")
    public ResponseEntity<SuccessResponseDto> updateImage(@RequestParam("file")MultipartFile image) {
        String username = securityUtils.getCurrentUsername();
        Long id = memberService.usernameToId(username);
        profileService.updateImage(id, image);

        return ResponseEntity.ok(new SuccessResponseDto("success update image"));
    }

    /**
     * 사용자가 원할 경우, 자신의 프로필 이미지를 기본 이미지로 바꾼다. 기본 이미지는 이미 저장되어 있다.
     * @return 성공 시 "success update basic image" 와 200 code 를 반환한다.
     * @tested yes
     */
    @PostMapping("/profile/basic-image")
    public ResponseEntity<SuccessResponseDto> updateToBasicImage() {
        String username = securityUtils.getCurrentUsername();
        Long id = memberService.usernameToId(username);

        profileService.updateImageToBasic(id);
        return ResponseEntity.ok(new SuccessResponseDto("success update basic image"));
    }

    /**
     * 저장된 이미지를 불러와 반환한다. 반환 타입은 byte[] 이며 http 메시지의 헤더를 MediaType.IMAGE_PNG 로 설정한다.
     * @return byte 로 구성된 이미지와 헤더, 200 code 를 반환한다.
     * @tested yes
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
     * @tested yes
     */
    @GetMapping("/profile")
    public ResponseEntity<ReadProfileDto> getProfile() {
        String username = securityUtils.getCurrentUsername();
        Long id = memberService.usernameToId(username);
        return ResponseEntity.ok(profileService.readProfile(id));
    }

    //TODO : not test yet.
    @GetMapping("/profile/image/url")
    public ResponseEntity<SuccessResponseDto> getImageUrl() {
        String username = securityUtils.getCurrentUsername();
        Long id = memberService.usernameToId(username);
        String url = profileService.getImageUrl(id);

        return ResponseEntity.ok(new SuccessResponseDto(url));
    }


    //TODO :  권한 문제 해결하기
    @PostMapping("/member/univ")
    public ResponseEntity<SuccessResponseDto> setMemberUniv(@Valid @RequestBody RequestSetUnivDto dto) {
        String username = securityUtils.getCurrentUsername();
        SetUniversityDto setUniversityDto = SetUniversityDto.builder()
                .username(username)
                .university(dto.getUniversity())
                .build();

        memberService.setUniversity(setUniversityDto);
        return ResponseEntity.ok(new SuccessResponseDto("success set university"));
    }
}
