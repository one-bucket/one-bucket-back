package com.onebucket.domain.boardManage.dto.internal.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * packageName : <span style="color: orange;">com.onebucket.domain.boardManage.dto.internal.board</span> <br>
 * name : <span style="color: orange;">BoardIdAndNameDto</span> <br>
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

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BoardIdAndNameDto {
    private Long id;
    private String name;
}
