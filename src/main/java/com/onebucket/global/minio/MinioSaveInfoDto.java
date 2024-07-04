package com.onebucket.global.minio;

import lombok.Builder;
import lombok.Getter;

/**
 * <br>package name   : com.onebucket.global.minio
 * <br>file name      : MinioSaveInfoDto
 * <br>date           : 2024-07-02
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * contain:
 * {@code
 *     private String bucketName;
 *     private String fileName;
 *     private String fileExtension;
 * }
 * </pre>
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
public class MinioSaveInfoDto {

    private String bucketName;
    private String fileName;
    private String fileExtension;

}
