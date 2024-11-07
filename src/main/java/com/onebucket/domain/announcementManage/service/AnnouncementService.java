package com.onebucket.domain.announcementManage.service;

import com.onebucket.domain.announcementManage.dto.AnnouncementDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * <br>package name   : com.onebucket.domain.announcementManage
 * <br>file name      : AnnouncementService
 * <br>date           : 11/6/24
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
public interface AnnouncementService {
    Page<AnnouncementDto.Thumbnail> getAnnouncementList(Pageable pageable);
    Long createAnnouncement(AnnouncementDto.Create dto);
    void deleteAnnouncement(Long id);
    AnnouncementDto.Info getAnnouncement(Long id);

}
