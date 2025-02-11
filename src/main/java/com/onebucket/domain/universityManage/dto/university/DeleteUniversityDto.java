package com.onebucket.domain.universityManage.dto.university;

import jakarta.validation.constraints.NotNull;

/**
 * <br>package name   : com.onebucket.domain.universityManage.dto.university
 * <br>file name      : DeleteUniversityDto
 * <br>date           : 2024-09-23
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
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-09-23        SeungHoon              init create
 * </pre>
 */
public record DeleteUniversityDto(
        @NotNull
        String name
) {
}
