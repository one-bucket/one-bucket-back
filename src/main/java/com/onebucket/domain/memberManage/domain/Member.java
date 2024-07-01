package com.onebucket.domain.memberManage.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>package name   : com.onebucket.member
 * <br>file name      : Member
 * <br>date           : 2024-06-22
 * <pre>
 * <span style="color: white;">[description]</span>
 * Basic Member entity, include id(generate automatically), username, password, nickname, and also
 * UserDetails params. Default value of all boolean variable is true, and "roles" is "GUEST".
 * All column is nullable.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 * Member member = Member.builder()
 *                  .username(username)
 *                  .password(password)
 *                  .nickname(nickname)
 *                  .build();
 * } </pre>
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-06-22     SeungHoon            init create
 * </pre>
 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique = true, updatable = false)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false, unique = true)
    private String nickname;

    @ElementCollection(fetch=FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>(List.of("GUEST"));

    @Column(nullable = false)
    @Builder.Default
    private boolean isAccountNonExpired = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean isAccountNonLocked = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean isCredentialNonExpired = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean isEnable = true;
}
