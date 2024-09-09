package com.onebucket.domain.boardManage.dao;

import com.onebucket.domain.boardManage.dto.internal.board.BoardIdAndNameDto;
import com.onebucket.domain.boardManage.dto.internal.board.BoardIdsDto;
import com.onebucket.domain.boardManage.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dao
 * <br>file name      : BoardRepository
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
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT DISTINCT new com.onebucket.domain.boardManage.dto.internal.board.BoardIdsDto(b.university.id, b.boardType.id) FROM Board b")
    List<BoardIdsDto> findAllBoardUniversityAndBoardTypeIds();

    @Query("SELECT new com.onebucket.domain.boardManage.dto.internal.board.BoardIdAndNameDto(b.id, b.name) FROM Board b WHERE b.university.id = ?")
    List<BoardIdAndNameDto> findBoardIdAndNameByUniversityId(Long id);


}
