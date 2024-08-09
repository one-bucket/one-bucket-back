package com.onebucket.domain.boardManage.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.internal
 * <br>file name      : BoardIdsDto
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

@AllArgsConstructor
@Setter
@Getter
public class BoardIdsDto {

    private Long universityId;
    private Long boardTypeId;
}
