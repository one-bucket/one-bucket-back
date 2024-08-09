package com.onebucket.domain.boardManage.dao;

import com.onebucket.domain.boardManage.entity.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dao
 * <br>file name      : BoardTypeRepository
 * <br>date           : 2024-07-18
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
 * 2024-07-18        jack8              init create
 * </pre>
 */
@Repository
public interface BoardTypeRepository extends JpaRepository<BoardType, Long> {
    Optional<BoardType> findByName(String name);

    @Query("""
            SELECT bt.id
            FROM BoardType bt
       """)
    List<Long> findAllBoardTypeIds();
}
