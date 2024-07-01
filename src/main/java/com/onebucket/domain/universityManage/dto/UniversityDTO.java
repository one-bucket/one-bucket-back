package com.onebucket.domain.universityManage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <br>package name   : com.onebucket.domain.universityManage.dto
 * <br>file name      : UniversityDTO
 * <br>date           : 2024-07-01
 * <pre>
 * <span style="color: white;">[description]</span>
 * 필요하지 않을 수 있지만 일단 추가해둠.
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
 * 2024-07-01        SeungHoon              init create
 * </pre>
 */
@Getter
@Setter
@Builder
public class UniversityDTO {
    @NotBlank(message = "name must not be empty")
    private String name;
}
