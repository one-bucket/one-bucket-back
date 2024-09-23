package com.onebucket.domain.boardManage.dto.response;

import com.onebucket.domain.boardManage.dto.internal.post.PostInfoDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.response
 * <br>file name      : ResponsePostDto
 * <br>date           : 2024-08-15
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
 * 2024-08-15        jack8              init create
 * </pre>
 */
@SuperBuilder
@Getter
@Setter
public class ResponsePostDto extends PostInfoDto {

    private boolean isUserAlreadyLikes;
}
