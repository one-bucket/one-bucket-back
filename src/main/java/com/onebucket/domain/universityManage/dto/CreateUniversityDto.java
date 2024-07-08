package com.onebucket.domain.universityManage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <br>package name   : com.onebucket.domain.universityManage.dto
 * <br>file name      : CreateUniversityDto
 * <br>date           : 2024-07-05
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
 * 2024-07-05        SeungHoon              init create
 * </pre>
 */
@Getter
@Setter
@Builder
public class CreateUniversityDto {
    @NotBlank(message = "name must not be empty")
    private String name;
    @NotBlank(message = "address must not be empty")
    private String address;
    @NotBlank(message = "email must not be empty")
    private String email;
}
