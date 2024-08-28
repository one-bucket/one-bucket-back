package com.onebucket.global.utils;

import lombok.*;

/**
 * <br>package name   : com.onebucket.global.utils
 * <br>file name      : SuccessResponseWithIdDto
 * <br>date           : 2024-08-08
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
 * 2024-08-08        jack8              init create
 * </pre>
 */

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponseWithIdDto {

    private String message;
    private Long id;
}
