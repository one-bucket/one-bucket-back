package com.onebucket.domain.universityManage.dto;

import lombok.*;

/**
 * <br>package name   : com.onebucket.domain.universityManage.dto
 * <br>file name      : ReadUniversityDto
 * <br>date           : 2024-07-05
 * <pre>
 * <span style="color: white;">[description]</span>
 * 클라이언트에서 대학 정보를 원하는 경우 사용하는 Dto이다.
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
@Builder
@Getter
@Setter
public class ResponseUniversityDto {
    private String name;
    private String address;
    private String email;
}
