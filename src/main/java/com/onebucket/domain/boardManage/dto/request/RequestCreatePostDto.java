package com.onebucket.domain.boardManage.dto.request;

import com.onebucket.domain.boardManage.dto.parents.PostDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto
 * <br>file name      : RequestCreatePostDto
 * <br>date           : 2024-08-05
 * <pre>
 * <span style="color: white;">[description]</span>
 * request dto to create post. Contain Getter method.
 * {@code
 * String board;
 * String title;
 * String text;
 * }
 * </pre>
 * <pre>
 */

@Getter
@SuperBuilder
public class RequestCreatePostDto extends PostDto {

}
