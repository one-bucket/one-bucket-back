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
 * board에 대한 기본적인 CRUD repository 와 특정 로직에 대한 커스텀 메서드
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 * List<BoardIdsDto> findAllBoardUniverisityAndBoardTypeids();
 *
 * List<BoardIdAndNameDto> findBoardIdAndNameByUniversityId(Long id);
 * } </pre>
 */

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    //모든 board 에 대하여 boardtype 과 university 의 쌍을 중복 없이 리스트로 반환
    @Query("SELECT DISTINCT new com.onebucket.domain.boardManage.dto.internal.board.BoardIdsDto(b.university.id, b.boardType.id) FROM Board b")
    List<BoardIdsDto> findAllBoardUniversityAndBoardTypeIds();

    //대학 id에 대하여 해당 id가 접근 가능한 모든 board id와 name 에 대한 리스트 반환
    @Query("SELECT new com.onebucket.domain.boardManage.dto.internal.board.BoardIdAndNameDto(b.id, b.name) FROM Board b WHERE b.university.id = ?1")
    List<BoardIdAndNameDto> findBoardIdAndNameByUniversityId(Long id);


}
