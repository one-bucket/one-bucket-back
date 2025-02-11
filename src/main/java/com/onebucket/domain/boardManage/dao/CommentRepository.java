package com.onebucket.domain.boardManage.dao;

import com.onebucket.domain.boardManage.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dao
 * <br>file name      : CommentRepository
 * <br>date           : 2024-07-12
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
 * 2024-07-12        jack8              init create
 * </pre>
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Long countAllByPostId(Long postId);
}
