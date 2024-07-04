package com.onebucket.domain.memberManage.dto;

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

    private String name;
    private String gender;
    private Integer age;
    private String description;
    private LocalDate birth;

}
