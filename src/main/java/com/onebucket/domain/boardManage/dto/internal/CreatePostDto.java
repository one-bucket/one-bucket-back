package com.onebucket.domain.boardManage.dto.internal;

import com.onebucket.domain.boardManage.dto.parents.PostDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.internal
 * <br>file name      : CreatePostDto
 * <br>date           : 2024-08-08
 * <pre>
 * <span style="color: white;">[description]</span>
 * Controller -> Service layer Dto. Used when create Post contain username which insert into Author field.
 * {@code
 *     private String username;
 *     private String board;
 *     private String title;
 *     private String text;}
 * </pre>
 */

@Getter
@SuperBuilder
@NoArgsConstructor
public class CreatePostDto extends PostDto {
    private String username;
}
