package com.onebucket.domain.boardManage.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @Size(min = 3, max = 10, message = "name of board must be over 3 under 10")
    private String name;

    @NotBlank
    @Size(min = 3, max = 10, message = "name of university must be over 3 under 10")
    private String university;

    @NotBlank
    @Size(min = 3, max = 10, message = "name of board type must be over 3 under 10")
    private String boardType;


    private String description;
}
