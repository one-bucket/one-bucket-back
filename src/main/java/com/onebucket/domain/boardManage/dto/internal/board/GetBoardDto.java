package com.onebucket.domain.boardManage.dto.internal.board;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;


/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.internal
 * <br>file name      : GetBoardDto
 * <br>date           : 2024-08-14
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
 * 2024-08-14        jack8              init create
 * </pre>
 */
@Builder
@Getter
public class GetBoardDto {
    private Long boardId;
    private Pageable pageable;
}
