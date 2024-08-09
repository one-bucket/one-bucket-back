package com.onebucket.domain.boardManage.dto.request;

import lombok.Builder;
import lombok.Getter;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.request
 * <br>file name      : ReqestCreateBoardDto
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

@Getter
@Builder
public class RequestCreateBoardDto {

    private String name;
    private String university;
    private String boardType;
    private String description;
}
