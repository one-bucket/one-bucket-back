package com.onebucket.domain.announcementManage.api;

import com.onebucket.domain.announcementManage.dto.AnnouncementDto;
import com.onebucket.domain.announcementManage.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <br>package name   : com.onebucket.domain.announcementManage.api
 * <br>file name      : AnnouncementConroller
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
@RequestMapping("/notice")
@RequiredArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;

    @GetMapping("/list")
    public ResponseEntity<Page<AnnouncementDto.Thumbnail>> getList(Pageable pageable) {
        return ResponseEntity.ok(announcementService.getAnnouncementList(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnouncementDto.Info> getAnnounce(@PathVariable Long id) {
        return ResponseEntity.ok(announcementService.getAnnouncement(id));
    }
}
