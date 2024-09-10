package com.onebucket.domain.boardManage.dto.parents;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.parents
 * <br>file name      : PostDto
 * <br>date           : 2024-08-08
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *     private Long boardId;
 *     private String title;
 *     private String text;
 * } </pre>
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public abstract class  PostDto {

    private Long boardId;
    private String title;
    private String text;
}
