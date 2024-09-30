package com.onebucket.domain.universityManage.dto.university;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <br>package name   : com.onebucket.domain.universityManage.dto
 * <br>file name      : UpdateUniversityDto
 * <br>date           : 2024-08-01
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
 * 2024-08-01        jack8              init create
 * </pre>
 */

@Getter
@Setter
@Builder
public class UpdateUniversityDto {
    private String address;
    private String name;
    @Email(message = "Email should be valid")
    private String email;
}
