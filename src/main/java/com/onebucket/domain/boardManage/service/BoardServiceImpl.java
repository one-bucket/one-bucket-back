package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dao.BoardRepository;
import com.onebucket.domain.boardManage.dao.BoardTypeRepository;
import com.onebucket.domain.boardManage.dto.internal.board.BoardIdsDto;
import com.onebucket.domain.boardManage.dto.internal.board.CreateBoardDto;
import com.onebucket.domain.boardManage.dto.internal.board.CreateBoardTypeDto;
import com.onebucket.domain.boardManage.dto.internal.board.CreateBoardsDto;
import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.boardManage.entity.BoardType;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.universityManage.dao.UniversityRepository;
import com.onebucket.domain.universityManage.domain.University;
import com.onebucket.global.exceptionManage.customException.boardManageException.AdminManageBoardException;
import com.onebucket.global.exceptionManage.customException.boardManageException.BoardManageException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.customException.universityManageException.UniversityManageException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.BoardErrorCode;
import com.onebucket.global.exceptionManage.errorCode.UniversityErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    private final BoardTypeRepository boardTypeRepository;
    private final MemberRepository memberRepository;
    private final UniversityRepository universityRepository;

    @Override
    public Long createBoard(CreateBoardDto dto) {

        University university = universityRepository.findByName(dto.getUniversity()).orElseThrow(() ->
                new UniversityManageException(UniversityErrorCode.NOT_EXIST_UNIVERSITY));

        BoardType boardType = boardTypeRepository.findByName(dto.getBoardType()).orElseThrow(() ->
                new BoardManageException(BoardErrorCode.UNKNOWN_BOARD_TYPE, "should add board type first"));

        Board newBoard = Board.builder()
                .name(dto.getName())
                .boardType(boardType)
                .university(university)
                .description(dto.getDescription())
                .build();

        try {
            return boardRepository.save(newBoard).getId();
        }catch (DataIntegrityViolationException e) {
            throw new AdminManageBoardException(BoardErrorCode.DUPLICATE_BOARD);
        }
    }

    /*TODO : maybe, need to change logic that create new Board.
       Too many search transaction on university and board type by same entity.
       Need to add caching method or algorithm.
     */
    @Override
    @Transactional
    public List<CreateBoardsDto> createBoards() {
        List<Long> boardTypeIds = boardTypeRepository.findAllBoardTypeIds();
        List<Long> universityIds = universityRepository.findAllUniversityIds();


        List<BoardIdsDto> savedBoardData = boardRepository.findAllBoardUniversityAndBoardTypeIds();

        Set<Long> setOfAll = ConcurrentHashMap.newKeySet();

        List<Board> boardsToSave = Collections.synchronizedList(new ArrayList<>());
        List<CreateBoardsDto> savedInfo = Collections.synchronizedList(new ArrayList<>());


        for(BoardIdsDto dto : savedBoardData) {
            Long key = generateCombinationKey(dto.getUniversityId(), dto.getBoardTypeId());

            setOfAll.add(key);
        }

        for(Long universityId : universityIds) {
            for(Long boardTypeId : boardTypeIds) {
                Long key = generateCombinationKey(universityId, boardTypeId);

                if(!setOfAll.contains(key)) {
                    University university = universityRepository.findById(universityId).orElseThrow(() ->
                            new UniversityManageException(UniversityErrorCode.NOT_EXIST_UNIVERSITY));
                    BoardType boardType = boardTypeRepository.findById(boardTypeId).orElseThrow(() ->
                            new BoardManageException(BoardErrorCode.UNKNOWN_BOARD_TYPE));
                    Board newBoard = Board.builder()
                            .university(university)
                            .boardType(boardType)
                            .name(university.getName() + "_" + boardType.getName())
                            .description("기본 메시지")
                            .build();

                    boardsToSave.add(newBoard);
                    setOfAll.add(key);
                }
            }
        }

        try {
            List<Board> savedBoard = boardRepository.saveAll(boardsToSave);
            for (Board board : savedBoard) {
                CreateBoardsDto boardInfo = CreateBoardsDto.builder()
                        .id(board.getId())
                        .boardName(board.getName())
                        .boardType(board.getBoardType().getName())
                        .university(board.getUniversity().getName())
                        .build();

                savedInfo.add(boardInfo);
            }

            return savedInfo;
        } catch(DataIntegrityViolationException e) {
            throw new BoardManageException(BoardErrorCode.DUPLICATE_BOARD);
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

    @Override
    public void createBoardType(CreateBoardTypeDto dto) {
        BoardType boardType = BoardType.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();

        try {
            boardTypeRepository.save(boardType);
        } catch(DataIntegrityViolationException e) {
            throw new AdminManageBoardException(BoardErrorCode.DUPLICATE_BOARD_TYPE);
        }
    }

    private Long generateCombinationKey(Long universityId, Long boardTypeId) {
        return universityId * 1000000L + boardTypeId;
    }
}
