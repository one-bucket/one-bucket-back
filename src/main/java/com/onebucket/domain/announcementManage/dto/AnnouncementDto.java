package com.onebucket.domain.announcementManage.dto;

import com.onebucket.domain.announcementManage.entity.Announcement;
import com.onebucket.domain.announcementManage.entity.NoticeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.announcementManage.dto
 * <br>file name      : AnnouncementDto
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
public class AnnouncementDto {


    @SuperBuilder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Base {
        private Long id;
        private String title;

        private String content;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;

    }

    @SuperBuilder
    @Getter
    @NoArgsConstructor
    public static class Thumbnail extends Base {


        // 썸네일 이므로 제목은 첨가하고 내용은 일부분만 첨가하자
        public static Thumbnail of(Announcement entity) {

            return Thumbnail.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .content(entity.getContent())
                    .createAt(entity.getCreateAt())
                    .updateAt(entity.getUpdateAt())
                    .build();
        }
    }

    @SuperBuilder
    public static class Info extends Base {
        private List<String> images;

        private List<String> files;

        public static Info of(Announcement entity) {
            return Info.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .content(entity.getContent())
                    .createAt(entity.getCreateAt())
                    .updateAt(entity.getUpdateAt())
                    .images(entity.getImages())
                    .files(entity.getFiles())
                    .build();
        }
    }


    @SuperBuilder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestCreate {
        private String title;
        private String content;
        private NoticeType noticeType;
    }

    @SuperBuilder
    @Getter
    @NoArgsConstructor
    public static class Create extends RequestCreate {
    }


}
