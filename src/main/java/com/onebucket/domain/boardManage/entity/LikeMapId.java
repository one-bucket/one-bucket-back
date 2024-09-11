package com.onebucket.domain.boardManage.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * <br>package name   : com.onebucket.domain.boardManage.entity
 * <br>file name      : LikeMapId
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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LikeMapId implements Serializable {
    private Long memberId;
    private Long postId;

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        LikeMapId likeMapId = (LikeMapId) o;
        return Objects.equals(memberId, likeMapId.memberId)
                && Objects.equals(postId, likeMapId.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, postId);
    }
}
