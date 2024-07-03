package com.onebucket.domain.memberManage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <br>package name   : com.onebucket.domain.memberManage.dto
 * <br>file name      : ReadProfileDto
 * <br>date           : 2024-07-03
 * <pre>
 * <span style="color: white;">[description]</span>
 * contain:
 * {@code
 *     private String name;
 *     private String gender;
 *     private int age;
 *     private String description;
 *     private LocalDate birth;
 *     private LocalDateTime createAt;
 *     private LocalDateTime updateAt;
 *     private String imageUrl;
 * }
 * </pre>
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-07-03        jack8              init create
 * </pre>
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReadProfileDto {

    private String name;
    private String gender;
    private int age;
    private String description;
    private LocalDate birth;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

}
