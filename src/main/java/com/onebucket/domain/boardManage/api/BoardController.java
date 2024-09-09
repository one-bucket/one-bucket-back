package com.onebucket.domain.boardManage.api;

import com.onebucket.domain.boardManage.dto.internal.board.BoardIdAndNameDto;
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
 * packageName : <span style="color: orange;">com.onebucket.domain.boardManage.api</span> <br>
 * name : <span style="color: orange;">BoardController</span> <br>
 * <p>
 * <span style="color: white;">[description]</span>
 * </p>
 * see Also: <br>
 *
 * <pre>
 * code usage:
 * {@code
 *
 * }
 * modified log:
 * ==========================================================
 * DATE          Author           Note
 * ----------------------------------------------------------
 * 9/9/24        isanghyeog         first create
 *
 * </pre>
 */

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;
    private final SecurityUtils securityUtils;

    @GetMapping("/list")
    public ResponseEntity<List<BoardIdAndNameDto>> getBoardList() {
        String username = securityUtils.getCurrentUsername();
        Long univId = memberService.usernameToUniversity(username).getId();

    }
}
