package com.onebucket.domain.memberManage.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.memberManage.domain
 * <br>file name      : MemberGroup
 * <br>date           : 10/29/24
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
@Builder
@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        name = "member_group",
        indexes = {
                @Index(name = "idx_member_id", columnList = "member_id")
        }
)
public class MemberGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "member_group_members",
            joinColumns = @JoinColumn(name = "member_group_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"),
            indexes = {
                    @Index(name = "idx_member_id", columnList = "member_id"),
                    @Index(name = "idx_member_group_member", columnList = "member_group_id, member_id")
            }
    )
    @Builder.Default
    private List<Member> members = new ArrayList<>();

    @Builder.Default
    private Integer referenceCount = 0; //해당 group을 얼마나 이용하고 있나

    public void addMember(Member member) {
        if(!members.contains(member)) {
            members.add(member);
        }
    }

    public void deleteMember(Member member) {
        members.remove(member);
    }


}
