package com.onebucket.domain.announcementManage.service;

import com.onebucket.domain.announcementManage.dao.AnnouncementRepository;
import com.onebucket.domain.announcementManage.dto.AnnouncementDto;
import com.onebucket.domain.announcementManage.entity.Announcement;
import com.onebucket.global.exceptionManage.customException.CommonException;
import com.onebucket.global.exceptionManage.errorCode.CommonErrorCode;
import com.onebucket.global.minio.MinioRepository;
import com.onebucket.global.minio.MinioInfoDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import org.springframework.web.multipart.MultipartFile;


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
    private final MinioRepository minioRepository;

    @Override
    public Page<AnnouncementDto.Thumbnail> getAnnouncementList(Pageable pageable) {
        Page<Announcement> pageAnnounce =  announcementRepository.findAll(pageable);

        return pageAnnounce.map(AnnouncementDto.Thumbnail::of);

    }

    @Override
    public Long createAnnouncement(AnnouncementDto.Create dto, List<MultipartFile> images, List<MultipartFile> files) {
        List<String> imageUrls = saveImagesInMinio(images);
        // 현재 pdf 형식이 아니라면 에러가 발생함
        List<String> fileUrls = saveFilesInMinio(files);

        Announcement announcement = Announcement.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .createAt(LocalDateTime.now())
                .images(imageUrls)
                .files(fileUrls)
                .noticeType(dto.getNoticeType())
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

    private List<String> saveImagesInMinio(List<MultipartFile> images) {
        return saveInMinio(images, "announcement/images");
    }

    private List<String> saveFilesInMinio(List<MultipartFile> files) {
        return saveInMinio(files,"announcement/files");
    }

    private List<String> saveInMinio(List<MultipartFile> multipartFiles, String path) {
        if(multipartFiles == null || multipartFiles.isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList<String> urls = new ArrayList<>();
        for (MultipartFile image : multipartFiles) {
            String originalFilename = image.getOriginalFilename();
            MinioInfoDto minioSaveInfoDto = MinioInfoDto.builder()
                            .fileName(path + "/" + originalFilename.split("\\.")[0])
                                    .fileExtension(originalFilename.split("\\.")[1])
                                            .bucketName("one-bucket")
                                                    .build();
            try {
                String url = minioRepository.uploadFile(image, minioSaveInfoDto);
                urls.add(url);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return urls;
    }
}
