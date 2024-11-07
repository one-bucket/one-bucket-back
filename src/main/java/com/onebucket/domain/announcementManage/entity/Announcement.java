package com.onebucket.domain.announcementManage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.anouncementManage.entity
 * <br>file name      : Anouncement
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
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Announcement {

    @Id
    private Long id;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String text;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "announcement_iamges",
            joinColumns = @JoinColumn(name = "announcement_id"))
    @Column(name = "image_url")
    @Builder.Default
    private List<String> image = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "announcement_files",
            joinColumns = @JoinColumn(name = "announcement_id"))
    @Column(name = "file_url")
    @Builder.Default
    private List<String> files = new ArrayList<>();

}
