package com.onebucket.domain.boardManage.entity;

import com.onebucket.domain.boardManage.entity.post.Post;
import com.onebucket.domain.memberManage.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <br>package name   : com.onebucket.domain.boardManage.entity
 * <br>file name      : LikesMap
 * <br>date           : 9/11/24
 * <pre>
 * <span style="color: white;">[description]</span>
 * 각 post의 좋아요 기능을 위한 엔티티 테이블이다. member와 post의 쌍으로 이루어져 있으며
 * primary key는 이 둘의 복합 키로 구성되어 있고 이를 위해 {@link LikesMapId}룰 사용하여
 * 복합 키를 설정하였다.해당 테이블에 데이터가 존재한다는 뜻은, 사용자가 해당 post에 좋아요를
 * 누른 것이다.
 * member_id에 대하여 index를 구성하였다.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 * private Member member;
 * private Post post;
 * } </pre>
 */

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(LikesMapId.class)
@Table(
        name = "likes_map",
        indexes = {
                @Index(name = "idx_member_id", columnList = "member_id")
        }
)
public class LikesMap {

    @Id
    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @Id
    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;
}
