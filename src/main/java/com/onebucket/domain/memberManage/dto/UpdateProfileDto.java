package com.onebucket.domain.memberManage.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * <br>package name   : com.onebucket.domain.memberManage.dto
 * <br>file name      : CreateProfileDto
 * <br>date           : 2024-07-02
 * <pre>
 * <span style="color: white;">[description]</span>
 * contain:
 * {@code
 *     private String name;
 *     private String gender;
 *     private Integer age;
 *     private String description;
 *     private LocalDate birth;}
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-07-02        jack8              init create
 * </pre>
 */

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileDto {


    @Size(min = 2, max = 5, message = "size of name must be over 2 under 5")
    private String name;

    @Pattern(regexp = "^(man|woman)$", message = "Gender must be either 'man' or 'woman'")
    private String gender;

    @Min(value= 0, message = "Age must be at least 0")
    @Max(value=110, message = "Age must be at most 110")
    private Integer age;

    @Size(max = 200, message = "Description must be at most 200 characters long")
    private String description;

    @Past(message = "Birth date must be in the past")
    private LocalDate birth;

}
