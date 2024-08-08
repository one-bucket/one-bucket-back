package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dto.CreateBoardDto;

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
    void createBoard(CreateBoardDto createBoardDto);
    boolean isValidBoard(String username, String boardId);
}
