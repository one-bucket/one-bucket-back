package com.onebucket.domain.PushMessageManage.Entity;

import com.onebucket.domain.memberManage.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <br>package name   : com.onebucket.domain.PushMessageManage.Entity
 * <br>file name      : DeviceToken
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
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(
        name = "device_token",
        indexes = {
                @Index(name = "idx_member_id", columnList = "member_id")
        }
)
public class DeviceToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Member member;

    private String deviceToken;

    private LocalDateTime updateAt;

    public void dateUpdate() {
        this.updateAt = LocalDateTime.now();
    }
}
