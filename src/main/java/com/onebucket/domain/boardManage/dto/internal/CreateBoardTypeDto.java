package com.onebucket.domain.boardManage.dto.internal;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.internal
 * <br>file name      : CreateBoardType
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

@SuperBuilder
@Getter
public class CreateBoardTypeDto {
    private String name;
    private String description;
}
