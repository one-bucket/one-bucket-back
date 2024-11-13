package com.onebucket.domain.announcementManage.api;

import com.onebucket.domain.announcementManage.dto.AnnouncementDto;
import com.onebucket.domain.announcementManage.dto.AnnouncementDto.RequestCreate;
import com.onebucket.domain.announcementManage.entity.NoticeType;
import com.onebucket.domain.announcementManage.service.AnnouncementService;
import com.onebucket.global.utils.SuccessResponseDto;
import com.onebucket.global.utils.SuccessResponseWithIdDto;
import java.util.List;
import javax.management.NotificationEmitter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <br>package name   : com.onebucket.domain.announcementManage.api
 * <br>file name      : AdminAnnouncementController
 * <br>date           : 11/7/24
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
@RequestMapping("/admin/notice")
@RequiredArgsConstructor
public class AdminAnnouncementController {

    private final AnnouncementService announcementService;

    @PostMapping("/create")
    public ResponseEntity<SuccessResponseWithIdDto> create(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam NoticeType noticeType,
            // 이름을 다르게 설정하자.
            @RequestParam(value = "image", required = false) List<MultipartFile> images,
            @RequestParam(value = "file", required = false) List<MultipartFile> files
    ) {
        AnnouncementDto.Create announcementCreateDto = AnnouncementDto.Create.builder()
                                                .content(content)
                                                .title(title)
                                                .noticeType(noticeType)
                                                .build();
        Long id = announcementService.createAnnouncement(announcementCreateDto,images,files);

        sendPushNotificationIfNeeded(noticeType);

        return ResponseEntity.ok(new SuccessResponseWithIdDto("success create announce", id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SuccessResponseDto> delete(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.ok(new SuccessResponseDto("success delete announcement"));
    }


    private void sendPushNotificationIfNeeded(NoticeType noticeType) {
        if(noticeType.equals(NoticeType.IMPORTANT)) {
            // 푸시 메시지 전송
        }
    }
}