package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dao.BoardRepository;
import com.onebucket.domain.boardManage.dto.CreateBoardDto;
import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.boardManage.entity.BoardType;
import com.onebucket.domain.universityManage.domain.University;
import com.onebucket.global.exceptionManage.customException.boardManageException.AdminManageBoardException;
import com.onebucket.global.exceptionManage.errorCode.BoardErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * <br>package name   : com.onebucket.domain.boardManage
 * <br>file name      : BoardService
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

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    public void createBoard(CreateBoardDto createBoardDto) {
        Board newBoard = Board.builder()
                .university(createBoardDto.getUniversity())
                .boardType(createBoardDto.getBoardType())
                .name(createBoardDto.getName())
                .description(createBoardDto.getDescription())
                .build();

        try {
            boardRepository.save(newBoard);
        }catch (DataIntegrityViolationException e) {
            throw new AdminManageBoardException(BoardErrorCode.DUPLICATE_BOARD);
        }

    }
}
