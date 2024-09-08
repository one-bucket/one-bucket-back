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
import com.onebucket.global.exceptionManage.customException.universityManageException.UniversityException;
import com.onebucket.global.exceptionManage.customException.universityManageException.UniversityManageException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.BoardErrorCode;
import com.onebucket.global.exceptionManage.errorCode.UniversityErrorCode;
import com.onebucket.testComponent.mockmember.MockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


/**
 * <br>package name   : com.onebucket.domain.boardManage.service
 * <br>file name      : BoardServiceTest
 * <br>date           : 2024-08-31
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
 * 2024-08-31        jack8              init create
 * </pre>
 */

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;
    @Mock
    private BoardTypeRepository boardTypeRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private UniversityRepository universityRepository;

    @Mock
    private University mockUniversity;
    @Mock
    private BoardType mockBoardType;
    @Mock
    private Board mockBoard;
    @Mock
    private Member mockMember;

    @InjectMocks
    private BoardServiceImpl boardService;

    private final Board savedBoard = Board.builder()
            .id(100L)
            .boardType(mockBoardType)
            .university(mockUniversity)
            .name("name")
            .description("description")
            .build();

    private final CreateBoardDto createBoardDto = CreateBoardDto.builder()
            .boardType("board type")
            .university("university")
            .name("name")
            .description("description")
            .build();



    @Test
    @DisplayName("createBoard - success")
    void testCreateBoard_success() {
        when(universityRepository.findByName(anyString())).thenReturn(Optional.of(mockUniversity));
        when(boardTypeRepository.findByName(anyString())).thenReturn(Optional.of(mockBoardType));
        when(boardRepository.save(any(Board.class))).thenReturn(savedBoard);


        Long id = boardService.createBoard(createBoardDto);
        assertThat(id).isEqualTo(100L);
    }

    @Test
    @DisplayName("createBoard - fail / unknown university")
    void testCreateBoard_fail_unknownUniv() {
        when(universityRepository.findByName(anyString())).thenReturn(Optional.empty());


        assertThatThrownBy(() -> boardService.createBoard(createBoardDto))
                .isInstanceOf(UniversityManageException.class)
                .extracting("errorCode")
                .isEqualTo(UniversityErrorCode.NOT_EXIST_UNIVERSITY);
        verify(boardTypeRepository, never()).findByName(anyString());
    }

    @Test
    @DisplayName("createBoard - fail / unknown boardType")
    void testCreateBoard_fail_unknownBoardType() {
        when(universityRepository.findByName(anyString())).thenReturn(Optional.of(mockUniversity));
        when(boardTypeRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> boardService.createBoard(createBoardDto))
                .isInstanceOf(BoardManageException.class)
                .hasMessageContaining("should add board type first")
                .extracting("errorCode")
                .isEqualTo(BoardErrorCode.UNKNOWN_BOARD_TYPE);

        verify(boardRepository, never()).save(any(Board.class));
    }

    @Test
    @DisplayName("createBoard - fail / duplicate board")
    void testCreateBoard_fail_duplicateBoard() {
        when(universityRepository.findByName(anyString())).thenReturn(Optional.of(mockUniversity));
        when(boardTypeRepository.findByName(anyString())).thenReturn(Optional.of(mockBoardType));

        when(boardRepository.save(any(Board.class))).thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> boardService.createBoard(createBoardDto))
                .isInstanceOf(AdminManageBoardException.class)
                .extracting("errorCode")
                .isEqualTo(BoardErrorCode.DUPLICATE_BOARD);
    }

    @Test
    @DisplayName("createBoards - success")
    void testCreateBoards_success() {
        List<Long> boardTypeIds = new ArrayList<>() {
            {
                for (Long i = 101L; i <= 110L; i++) {
                    add(i);
                }
            }
        };

        List<Long> universityIds = new ArrayList<>() {
            {
                for (Long i = 101L; i <= 110L; i++) {
                    add(i);
                }
            }
        };

        List<BoardIdsDto> boardIdsDtos = new ArrayList<>() {
            {
                for (Long i = 101L; i <= 103L; i++) {
                    for (Long j = 101L; j <= 104L; j++) {
                        BoardIdsDto boardIdsDto = BoardIdsDto.builder()
                                .boardTypeId(i)
                                .universityId(j)
                                .build();
                        add(boardIdsDto);
                    }

                }
            }
        };

        when(boardTypeRepository.findAllBoardTypeIds()).thenReturn(boardTypeIds);
        when(universityRepository.findAllUniversityIds()).thenReturn(universityIds);
        when(boardRepository.findAllBoardUniversityAndBoardTypeIds()).thenReturn(boardIdsDtos);

        for (long i = 101L; i <= 110L; i++) {
            when(universityRepository.findById(i)).thenReturn(Optional.of(mockUniversity));

        }
        for (long j = 101L; j <= 110L; j++) {
            when(boardTypeRepository.findById(j)).thenReturn(Optional.of(mockBoardType));
        }

        boardService.createBoards();
        ArgumentCaptor<List<Board>> captor = ArgumentCaptor.forClass(List.class);
        verify(boardRepository).saveAll(captor.capture());

        List<Board> capturedBoards = captor.getValue();


        assertThat(capturedBoards).isNotNull();
        assertThat(capturedBoards.size()).isEqualTo(88);
    }

    @Test
    @DisplayName("testCreateBoards - success / all board already exist")
    void testCreateBoards_success_allBoardAlreadyExist() {
        List<Long> boardTypeIds = new ArrayList<>() {
            {
                for (Long i = 101L; i <= 110L; i++) {
                    add(i);
                }
            }
        };

        List<Long> universityIds = new ArrayList<>() {
            {
                for (Long i = 101L; i <= 110L; i++) {
                    add(i);
                }
            }
        };

        List<BoardIdsDto> boardIdsDtos = new ArrayList<>() {
            {
                for (Long i = 101L; i <= 110L; i++) {
                    for (Long j = 101L; j <= 110L; j++) {
                        BoardIdsDto boardIdsDto = BoardIdsDto.builder()
                                .boardTypeId(i)
                                .universityId(j)
                                .build();
                        add(boardIdsDto);
                    }

                }
            }
        };

        when(boardTypeRepository.findAllBoardTypeIds()).thenReturn(boardTypeIds);
        when(universityRepository.findAllUniversityIds()).thenReturn(universityIds);
        when(boardRepository.findAllBoardUniversityAndBoardTypeIds()).thenReturn(boardIdsDtos);

        List<CreateBoardsDto> result = boardService.createBoards();
        ArgumentCaptor<List<Board>> captor = ArgumentCaptor.forClass(List.class);
        verify(boardRepository).saveAll(captor.capture());

        List<Board> capturedBoards = captor.getValue();


        assertThat(capturedBoards.size()).isEqualTo(0);
        assertThat(result.size()).isEqualTo(0);

        verify(universityRepository, never()).findById(anyLong());
        verify(boardTypeRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("createBoards - success / not exist boardtype yet")
    void testCreateBoards_success_notExistBoardType() {
        List<Long> universityIds = new ArrayList<>() {
            {
                for (Long i = 101L; i <= 110L; i++) {
                    add(i);
                }
            }
        };

        when(boardTypeRepository.findAllBoardTypeIds()).thenReturn(Collections.emptyList());
        when(universityRepository.findAllUniversityIds()).thenReturn(universityIds);
        when(boardRepository.findAllBoardUniversityAndBoardTypeIds()).thenReturn(Collections.emptyList());


        List<CreateBoardsDto> result = boardService.createBoards();
        ArgumentCaptor<List<Board>> captor = ArgumentCaptor.forClass(List.class);
        verify(boardRepository).saveAll(captor.capture());

        List<Board> capturedBoards = captor.getValue();


        assertThat(capturedBoards.size()).isEqualTo(0);
        assertThat(result.size()).isEqualTo(0);

        verify(universityRepository, never()).findById(anyLong());
        verify(boardTypeRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("createBoards - fail / unknown board type")
    void testCreateBoards_fail_unknownBoardType() {
        List<Long> boardTypeIds = new ArrayList<>() {
            {
                for (Long i = 101L; i <= 110L; i++) {
                    add(i);
                }
            }
        };

        List<Long> universityIds = new ArrayList<>() {
            {
                for (Long i = 101L; i <= 110L; i++) {
                    add(i);
                }
            }
        };

        List<BoardIdsDto> boardIdsDtos = new ArrayList<>() {
            {
                for (Long i = 101L; i <= 103L; i++) {
                    for (Long j = 101L; j <= 104L; j++) {
                        BoardIdsDto boardIdsDto = BoardIdsDto.builder()
                                .boardTypeId(i)
                                .universityId(j)
                                .build();
                        add(boardIdsDto);
                    }

                }
            }
        };

        when(boardTypeRepository.findAllBoardTypeIds()).thenReturn(boardTypeIds);
        when(universityRepository.findAllUniversityIds()).thenReturn(universityIds);
        when(boardRepository.findAllBoardUniversityAndBoardTypeIds()).thenReturn(boardIdsDtos);

        when(universityRepository.findById(anyLong())).thenReturn(Optional.of(mockUniversity));
        when(boardTypeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> boardService.createBoards())
                .isInstanceOf(BoardManageException.class)
                .extracting("errorCode")
                .isEqualTo(BoardErrorCode.UNKNOWN_BOARD_TYPE);

        verify(boardRepository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("createBoards - fail / unknown university")
    void testCreateBoards_fail_unknownUniversity() {
        List<Long> boardTypeIds = new ArrayList<>() {
            {
                for (Long i = 101L; i <= 110L; i++) {
                    add(i);
                }
            }
        };

        List<Long> universityIds = new ArrayList<>() {
            {
                for (Long i = 101L; i <= 110L; i++) {
                    add(i);
                }
            }
        };

        List<BoardIdsDto> boardIdsDtos = new ArrayList<>() {
            {
                for (Long i = 101L; i <= 103L; i++) {
                    for (Long j = 101L; j <= 104L; j++) {
                        BoardIdsDto boardIdsDto = BoardIdsDto.builder()
                                .boardTypeId(i)
                                .universityId(j)
                                .build();
                        add(boardIdsDto);
                    }

                }
            }
        };

        when(boardTypeRepository.findAllBoardTypeIds()).thenReturn(boardTypeIds);
        when(universityRepository.findAllUniversityIds()).thenReturn(universityIds);
        when(boardRepository.findAllBoardUniversityAndBoardTypeIds()).thenReturn(boardIdsDtos);

        when(universityRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> boardService.createBoards())
                .isInstanceOf(UniversityManageException.class)
                .extracting("errorCode")
                .isEqualTo(UniversityErrorCode.NOT_EXIST_UNIVERSITY);

        verify(boardRepository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("createBoards - fail / unknown university")
    void testCreateBoards_fail_duplicateBoards() {
        List<Long> boardTypeIds = new ArrayList<>() {
            {
                for (Long i = 101L; i <= 110L; i++) {
                    add(i);
                }
            }
        };

        List<Long> universityIds = new ArrayList<>() {
            {
                for (Long i = 101L; i <= 110L; i++) {
                    add(i);
                }
            }
        };

        List<BoardIdsDto> boardIdsDtos = new ArrayList<>() {
            {
                for (Long i = 101L; i <= 103L; i++) {
                    for (Long j = 101L; j <= 104L; j++) {
                        BoardIdsDto boardIdsDto = BoardIdsDto.builder()
                                .boardTypeId(i)
                                .universityId(j)
                                .build();
                        add(boardIdsDto);
                    }

                }
            }
        };

        when(boardTypeRepository.findAllBoardTypeIds()).thenReturn(boardTypeIds);
        when(universityRepository.findAllUniversityIds()).thenReturn(universityIds);
        when(boardRepository.findAllBoardUniversityAndBoardTypeIds()).thenReturn(boardIdsDtos);

        when(universityRepository.findById(anyLong())).thenReturn(Optional.of(mockUniversity));
        when(boardTypeRepository.findById(anyLong())).thenReturn(Optional.of(mockBoardType));
        when(boardRepository.saveAll(anyList())).thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> boardService.createBoards())
                .isInstanceOf(BoardManageException.class)
                .extracting("errorCode")
                .isEqualTo(BoardErrorCode.DUPLICATE_BOARD);

    }

    @Test
    @DisplayName("isValidBoard - success / true")
    void testIsValidBoard_success_true() {
        String username = "username";
        String boardId = "1";
        String universityName = "university";
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));
        when(mockMember.getUniversity()).thenReturn(mockUniversity);
        when(mockUniversity.getName()).thenReturn(universityName);

        when(boardRepository.findById(Long.parseLong(boardId))).thenReturn(Optional.of(mockBoard));
        when(mockBoard.getUniversity()).thenReturn(mockUniversity);

        boolean result = boardService.isValidBoard(username, boardId);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("isValidBoard - success / false")
    void testIsValidBoard_success_false() {
        String username = "username";
        String boardId = "1";
        String universityName = "university";

        University unSameUniversity = Mockito.mock(University.class);
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));
        when(mockMember.getUniversity()).thenReturn(mockUniversity);
        when(mockUniversity.getName()).thenReturn(universityName);

        when(boardRepository.findById(Long.parseLong(boardId))).thenReturn(Optional.of(mockBoard));
        when(mockBoard.getUniversity()).thenReturn(unSameUniversity);
        when(unSameUniversity.getName()).thenReturn("universities");

        boolean result = boardService.isValidBoard(username, boardId);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("isValidBoard - fail / unknown member")
    void testIsValidBoard_fail_unknownMember() {
        String username = "username";
        String boardId = "1";
        String universityName = "university";

        when(memberRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> boardService.isValidBoard(username, boardId))
                .isInstanceOf(AuthenticationException.class)
                .extracting("errorCode")
                .isEqualTo(AuthenticationErrorCode.UNKNOWN_USER);

        verify(boardRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("isValidBoard - fail / unknwon board")
    void testIsValidBoard_fail_unknownBoard() {
        String username = "username";
        String boardId = "1";
        String universityName = "university";
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));
        when(mockMember.getUniversity()).thenReturn(mockUniversity);
        when(mockUniversity.getName()).thenReturn(universityName);

        when(boardRepository.findById(Long.parseLong(boardId))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> boardService.isValidBoard(username, boardId))
                .isInstanceOf(BoardManageException.class)
                .extracting("errorCode")
                .isEqualTo(BoardErrorCode.UNKNOWN_BOARD);

        verify(mockUniversity.equals(any(University.class)));
    }

    @Test
    @DisplayName("createBoardType_success")
    void testCreateBoardType_success() {
        CreateBoardTypeDto dto = CreateBoardTypeDto.builder()
                .name("name")
                .description("description")
                .build();


        boardService.createBoardType(dto);
        ArgumentCaptor<BoardType> captor = ArgumentCaptor.forClass(BoardType.class);
        verify(boardTypeRepository).save(captor.capture());

        BoardType captorValue = captor.getValue();

        assertThat(captorValue.getName()).isEqualTo("name");
        assertThat(captorValue.getDescription()).isEqualTo("description");

    }

    @Test
    @DisplayName("createBoardType_fail")
    void testCreateBoardType_fail_duplicateBoardType() {
        CreateBoardTypeDto dto = CreateBoardTypeDto.builder()
                .name("name")
                .description("description")
                .build();

        when(boardTypeRepository.save(any(BoardType.class))).thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> boardService.createBoardType(dto))
                .isInstanceOf(AdminManageBoardException.class)
                .extracting("errorCode")
                .isEqualTo(BoardErrorCode.DUPLICATE_BOARD_TYPE);

    }
}