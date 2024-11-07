package com.onebucket.domain.announcementManage.service;

import com.onebucket.domain.announcementManage.dao.AnnouncementRepository;
import com.onebucket.domain.announcementManage.dto.AnnouncementDto;
import com.onebucket.domain.announcementManage.entity.Announcement;
import com.onebucket.global.exceptionManage.customException.CommonException;
import com.onebucket.global.exceptionManage.errorCode.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


/**
 * <br>package name   : com.onebucket.domain.announcementManage.service
 * <br>file name      : AnnouncementServiceImpl
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

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl  implements AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    @Override
    public Page<AnnouncementDto.Thumbnail> getAnnouncementList(Pageable pageable) {
        Page<Announcement> pageAnnounce =  announcementRepository.findAll(pageable);

        return pageAnnounce.map(AnnouncementDto.Thumbnail::of);

    }

    @Override
    public Long createAnnouncement(AnnouncementDto.Create dto) {
        Announcement announcement = Announcement.builder()
                .title(dto.getTitle())
                .text(dto.getText())
                .createAt(LocalDateTime.now())
                .build();

        return announcementRepository.save(announcement).getId();
    }

    @Override
    public void deleteAnnouncement(Long id) {
        announcementRepository.deleteById(id);
    }

    @Override
    public AnnouncementDto.Info getAnnouncement(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new CommonException(CommonErrorCode.DATA_ACCESS_ERROR));

        return AnnouncementDto.Info.of(announcement);
    }

}
