package com.onebucket.domain.boardManage.entity;

import com.onebucket.domain.boardManage.entity.post.Post;
import com.onebucket.domain.memberManage.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * <br>package name   : com.onebucket.domain.boardManage.entity
 * <br>file name      : LikesMap
 * <br>date           : 9/11/24
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
@NoArgsConstructor
@AllArgsConstructor
@IdClass(LikeMapId.class)
@Table(
        name = "likes_map",
        indexes = {

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
