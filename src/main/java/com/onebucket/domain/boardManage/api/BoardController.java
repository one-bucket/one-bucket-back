package com.onebucket.domain.boardManage.api;

import com.onebucket.domain.boardManage.dto.internal.board.CreateBoardDto;
import com.onebucket.domain.boardManage.dto.internal.board.CreateBoardTypeDto;
import com.onebucket.domain.boardManage.dto.internal.board.CreateBoardsDto;
import com.onebucket.domain.boardManage.dto.request.RequestCreateBoardDto;
import com.onebucket.domain.boardManage.dto.request.RequestCreateBoardTypeDto;
import com.onebucket.domain.boardManage.dto.response.ResponseCreateBoardsDto;
import com.onebucket.domain.boardManage.service.BoardService;
import com.onebucket.global.utils.SuccessResponseDto;
import com.onebucket.global.utils.SuccessResponseWithIdDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.api
 * <br>file name      : BoardController
 * <br>date           : 2024-08-08
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
 * 2024-08-08        jack8              init create
 * </pre>
 */

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/create")
    ResponseEntity<SuccessResponseWithIdDto> createBoard(@RequestBody @Valid RequestCreateBoardDto dto) {
        CreateBoardDto createBoardDto = CreateBoardDto.builder()
                .name(dto.getName())
                .boardType(dto.getBoardType())
                .university(dto.getUniversity())
                .description(dto.getDescription())
                .build();

        Long id = boardService.createBoard(createBoardDto);

        return ResponseEntity.ok(new SuccessResponseWithIdDto("success create board", id));
    }

    @PostMapping("/creates")
    ResponseEntity<List<ResponseCreateBoardsDto>> createBoards() {
        List<CreateBoardsDto> results = boardService.createBoards();

        List<ResponseCreateBoardsDto> responses = Collections.synchronizedList(new ArrayList<>());

        for(CreateBoardsDto result : results) {
            ResponseCreateBoardsDto response = ResponseCreateBoardsDto.builder()
                    .id(result.getId())
                    .boardName(result.getBoardName())
                    .boardType(result.getBoardType())
                    .university(result.getUniversity())
                    .build();

            responses.add(response);
        }

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/type")
    ResponseEntity<SuccessResponseDto> createBoardType(@RequestBody @Valid RequestCreateBoardTypeDto dto) {
        CreateBoardTypeDto createBoardTypeDto = CreateBoardTypeDto.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();

        boardService.createBoardType(createBoardTypeDto);

        return ResponseEntity.ok(new SuccessResponseDto("success create board type"));
    }

}
