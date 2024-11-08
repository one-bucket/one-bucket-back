package com.onebucket.domain.announcementManage.api;

import com.onebucket.domain.announcementManage.dto.AnnouncementDto;
import com.onebucket.domain.announcementManage.dto.AnnouncementDto.RequestCreate;
import com.onebucket.domain.announcementManage.service.AnnouncementService;
import com.onebucket.global.utils.SuccessResponseDto;
import com.onebucket.global.utils.SuccessResponseWithIdDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<SuccessResponseWithIdDto> create(@RequestBody AnnouncementDto.RequestCreate dto) {
        AnnouncementDto.Create announcementCreateDto = AnnouncementDto.Create.builder()
                                                .text(dto.getText())
                                                .title(dto.getTitle())
                                                .build();
        Long id = announcementService.createAnnouncement(announcementCreateDto);

        sendPushNotificationIfNeeded(dto);

        return ResponseEntity.ok(new SuccessResponseWithIdDto("success create announce", id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SuccessResponseDto> delete(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.ok(new SuccessResponseDto("success delete announcement"));
    }


    private void sendPushNotificationIfNeeded(RequestCreate dto) {
        if(Boolean.TRUE.equals(dto.getSendPushMessage())) {
            // 푸시 알림 메세지 전송
        }
    }
}