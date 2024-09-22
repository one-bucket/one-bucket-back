package com.onebucket.domain.boardManage.service;


import com.onebucket.domain.boardManage.dto.internal.board.BoardIdAndNameDto;
import com.onebucket.domain.boardManage.dto.internal.board.CreateBoardDto;
import com.onebucket.domain.boardManage.dto.internal.board.CreateBoardTypeDto;
import com.onebucket.domain.boardManage.dto.internal.board.CreateBoardsDto;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.service
 * <br>file name      : BoardService
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
public interface BoardService {
    Long createBoard(CreateBoardDto dto);

    List<CreateBoardsDto> createBoards();
    boolean isValidBoard(String username, String boardId);

    void createBoardType(CreateBoardTypeDto dto);
    List<BoardIdAndNameDto>getBoardList(Long univId);

    String getType(Long boardId);
}
