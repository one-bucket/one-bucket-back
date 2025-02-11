package com.onebucket.domain.universityManage.dto.university;

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
 * private String name;
 * private String address;
 * private String email;
 * </pre>
 * <pre>
 */
@Getter
@Setter
@Builder
public class RequestUniversityDto {
    @NotBlank(message = "name must not be empty")
    private String name;

    @NotBlank(message = "address must not be empty")
    private String address;

    @NotBlank(message = "email must not be empty")
    private String email;
}
