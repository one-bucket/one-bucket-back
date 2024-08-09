package com.onebucket.domain.boardManage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.response
 * <br>file name      : ResponseCreatesBoardDto
 * <br>date           : 2024-08-09
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
 * 2024-08-09        jack8              init create
 * </pre>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ResponseCreateBoardsDto {
    private Long id;
    private String boardName;
    private String university;
    private String boardType;

}
