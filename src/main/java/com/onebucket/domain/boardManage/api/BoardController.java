package com.onebucket.domain.boardManage.api;

import com.onebucket.domain.boardManage.dto.internal.board.BoardIdAndNameDto;
import com.onebucket.domain.boardManage.dto.response.ResponseBoardIdAndNameDto;
import com.onebucket.domain.boardManage.service.BoardService;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.global.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.api
 * <br>file name      : BoardController
 * <br>date           : 2024-09-09
 * <pre>
 * <span style="color: white;">[description]</span>
 * admin이 아닌 사용자 시점에서 사용되는 API에 대한 컨트롤러이다. 구현된 코드는 다음과 같다.
 * GET /board/list
 *    -> List<{@link ResponseBoardIdAndNameDto}>
 * </pre>
 */

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;
    private final SecurityUtils securityUtils;


    /**
     * 현재 사용자가 접근 가능한 board 의 list를 반환한다. 사용자의 대학 정보를 기반으로 해당하는 board를 가져온다.
     *
     * @tested true
     * @return List of name and id of board
     */
    @GetMapping("/list")
    public ResponseEntity<List<ResponseBoardIdAndNameDto>> getBoardList() {
        String username = securityUtils.getCurrentUsername();
        Long univId = memberService.usernameToUniversity(username).getId();

        List<BoardIdAndNameDto> boardLists = boardService.getBoardList(univId);

        List<ResponseBoardIdAndNameDto> results = boardLists.stream().map(this::convertToResponseDto).toList();

        return ResponseEntity.ok(results);

    }

    private ResponseBoardIdAndNameDto convertToResponseDto(BoardIdAndNameDto dto) {
        String type = boardService.getType(dto.getId());
        return ResponseBoardIdAndNameDto.builder()
                .id(dto.getId())
                .name(dto.getName())
                .type(type)
                .build();
    }
}
