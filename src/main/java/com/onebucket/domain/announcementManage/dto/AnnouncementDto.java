package com.onebucket.domain.announcementManage.dto;

import com.onebucket.domain.announcementManage.entity.Announcement;
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

        private String text;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;

    }

    @SuperBuilder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Thumbnail extends Base {

        public static Thumbnail of(Announcement entity) {

            return Thumbnail.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .text(entity.getText())
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
                    .text(entity.getText())
                    .createAt(entity.getCreateAt())
                    .updateAt(entity.getUpdateAt())
                    .images(entity.getImage())
                    .files(entity.getFiles())
                    .build();
        }
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestCreate {
        private String title;
        private String text;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create extends RequestCreate {

    }


}
