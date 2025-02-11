package com.onebucket.domain.boardManage.dao;

import com.onebucket.domain.boardManage.entity.LikesMapId;
import com.onebucket.domain.boardManage.entity.LikesMap;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dao
 * <br>file name      : LikesMapRepository
 * <br>date           : 2024-09-11
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
@Repository
public interface LikesMapRepository extends JpaRepository<LikesMap, LikesMapId> {
    Page<LikesMap> findByMemberId(Pageable pageable, Long memberId);

    long countByPostId(Long postId);

    void deleteById(@NotNull LikesMapId likesMapId);

}
