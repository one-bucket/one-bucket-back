package com.onebucket.domain.boardManage.dto;

import lombok.Getter;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto
 * <br>file name      : RequestCreatePostDto
 * <br>date           : 2024-08-05
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
 * 2024-08-05        jack8              init create
 * </pre>
 */

@Getter
public class CreatePostDto {
    private String board;
    private String title;
    private String text;
}
