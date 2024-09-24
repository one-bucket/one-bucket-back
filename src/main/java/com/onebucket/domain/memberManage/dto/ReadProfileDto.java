package com.onebucket.domain.memberManage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
@Builder(builderClassName = "ReadProfileDtoBuilder")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReadProfileDto {

    private String name;
    private String gender;
    private int age;
    private String description;
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birth;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updateAt;

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt.truncatedTo(ChronoUnit.SECONDS);
    }
    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt.truncatedTo(ChronoUnit.SECONDS);
    }

    public static class ReadProfileDtoBuilder {
        public ReadProfileDtoBuilder createAt(LocalDateTime createAt) {
            this.createAt = createAt.truncatedTo(ChronoUnit.SECONDS);
            return this;
        }

        public ReadProfileDtoBuilder updateAt(LocalDateTime updateAt) {
            this.updateAt = updateAt.truncatedTo(ChronoUnit.SECONDS);
            return this;
        }
    }
}
