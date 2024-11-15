package com.onebucket.domain.announcementManage.service;

import com.onebucket.domain.announcementManage.dto.AnnouncementDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


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
    Long createAnnouncement(AnnouncementDto.Create dto, List<MultipartFile> images,List<MultipartFile> files);
    void deleteAnnouncement(Long id);
    AnnouncementDto.Info getAnnouncement(Long id);

}
