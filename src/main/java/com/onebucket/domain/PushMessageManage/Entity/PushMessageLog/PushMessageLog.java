package com.onebucket.domain.PushMessageManage.Entity.PushMessageLog;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * <br>package name   : com.onebucket.domain.PushMessageManage.Entity
 * <br>file name      : PushMessageLog
 * <br>date           : 11/5/24
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
@Document(collation = "{ locale: 'ko' }")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushMessageLog {

    @Id
    private Long id;

    private String title;
    private String body;

    private String image;

    private LocalDateTime sendAt;

    private String sendTo;

}
