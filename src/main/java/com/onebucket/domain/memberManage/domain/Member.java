package com.onebucket.domain.memberManage.domain;

import com.onebucket.domain.universityManage.domain.University;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "university_id")
    private University university;

    @ElementCollection(fetch=FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    public void addRoles(String auth) {
        this.roles.add(auth);
    }

    @OneToOne(fetch =FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private Profile profile;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
