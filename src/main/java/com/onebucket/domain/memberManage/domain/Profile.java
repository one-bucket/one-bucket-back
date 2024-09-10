package com.onebucket.domain.memberManage.domain;

import com.onebucket.domain.WalletManage.domain.Wallet;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <br>package name   : com.onebucket.domain.memberManage.domain
 * <br>file name      : Profile
 * <br>date           : 2024-07-01
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-07-01        jack8              init create
 * </pre>
 */

@Builder
@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Profile {

    @Id
    @Column(nullable = false, updatable = false)
    private Long id;

    private String name;
    private String gender;
    private Integer age;
    private String description;
    private LocalDate birth;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private boolean isBasicImage;

    private String imageUrl;

    @OneToOne(mappedBy = "profile",fetch = FetchType.LAZY)
    private Wallet wallet;
}
