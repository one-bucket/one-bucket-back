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
 * {@link LikesMap} 의 복합키 설정을 위한 객체. 객체의 값 비교를 위해 equals와 hashcode를 override하여
 * 구현하였다.
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
public class LikesMapId implements Serializable {
    private Long member;
    private Long post;

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        LikesMapId likesMapId = (LikesMapId) o;
        return Objects.equals(member, likesMapId.member)
                && Objects.equals(post, likesMapId.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, post);
    }
}
