package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dao.BoardRepository;
import com.onebucket.domain.boardManage.dto.CreateBoardDto;
import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.boardManage.entity.BoardType;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.universityManage.domain.University;
import com.onebucket.global.exceptionManage.customException.boardManageException.AdminManageBoardException;
import com.onebucket.global.exceptionManage.customException.boardManageException.BoardManageException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.BoardErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    private final MemberRepository memberRepository;

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

    @Override
    public boolean isValidBoard(String username, String boardId) {
        Member member = memberRepository.findByUsername(username).orElseThrow(() ->
                new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));

        String userUniv = member.getUniversity().getName();
        String boardUniv = boardRepository.findById(Long.parseLong(boardId)).orElseThrow(() ->
                new BoardManageException(BoardErrorCode.UNKNOWN_BOARD))
                .getUniversity().getName();

        return userUniv.equals(boardUniv);

    }
}
