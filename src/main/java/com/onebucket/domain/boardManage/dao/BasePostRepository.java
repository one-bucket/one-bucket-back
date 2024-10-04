package com.onebucket.domain.boardManage.dao;

import com.onebucket.domain.boardManage.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dao
 * <br>file name      : BasePostRepository
 * <br>date           : 2024-09-21
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
public interface BasePostRepository<T extends Post> extends JpaRepository<T, Long> {
    Page<T> findByBoardId(Long boardId, Pageable pageable);

    @Modifying
    @Query("UPDATE Post p SET p.views = p.views + 1 WHERE p.id = :id")
    void increaseView(@Param("id") Long id);

    //이후 로그로 얼만큼 변환되었는지에 대해 알고 싶으면 int로 반환타입을 설정해주어 갱신된 행의 개수를 파악할 수 있음
    @Modifying
    @Transactional
    @Query("""
            UPDATE Post p
            SET p.likes = CASE WHEN p.likes + :increment < 0 THEN 0 ELSE p.likes + :increment END
            WHERE p.id = :postId
            """)
    void updateLikes(@Param("postId") Long postId, @Param("increment") Long increment);

    @Modifying
    @Query("UPDATE Post p SET p.imageUrls = CONCAT(p.imageUrls, :imageUrl) WHERE p.id = :postId")
    void addImageUrl(@Param("postId") Long postId, @Param("imageUrl") String imageUrl);

    //search query
    Page<T> titleSearchResult(@Param("keyword") String keyword, Pageable pageable);
}
