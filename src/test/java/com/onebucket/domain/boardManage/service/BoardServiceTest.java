package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dao.BoardRepository;
import com.onebucket.domain.boardManage.dao.BoardTypeRepository;
import com.onebucket.domain.boardManage.dto.internal.board.CreateBoardDto;
import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.boardManage.entity.BoardType;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.universityManage.dao.UniversityRepository;
import com.onebucket.domain.universityManage.domain.University;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


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

    @InjectMocks
    private BoardServiceImpl boardService;

    private final Board savedBoard = Board.builder()
            .id(100L)
            .boardType(mockBoardType)
            .university(mockUniversity)
            .name("name")
            .description("description")
            .build();


    @Test
    @DisplayName("createBoard - success")
    void testCreateBoard_success() {
        when(universityRepository.findByName(anyString())).thenReturn(Optional.of(mockUniversity));
        when(boardTypeRepository.findByName(anyString())).thenReturn(Optional.of(mockBoardType));
        when(boardRepository.save(any(Board.class))).thenReturn(savedBoard);

        CreateBoardDto dto = CreateBoardDto.builder()
                .boardType("board type")
                .university("university")
                .name("name")
                .description("description")
                .build();

        Long id = boardService.createBoard(dto);
        assertThat(id).isEqualTo(100L);
    }



}