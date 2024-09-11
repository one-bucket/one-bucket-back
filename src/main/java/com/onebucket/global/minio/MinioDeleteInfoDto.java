package com.onebucket.global.minio;

/**
 * <br>package name   : com.onebucket.global.minio
 * <br>file name      : MinioDeleteInfoDto
 * <br>date           : 2024-09-11
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
 * 2024-09-11        SeungHoon              init create
 * </pre>
 */
public record MinioDeleteInfoDto(
        String bucketName,
        String fileName,
        String fileExtension,
        String roomId
) {
    public static MinioDeleteInfoDto of(String bucketName, String fileName, String fileExtension, String roomId) {
        return new MinioDeleteInfoDto(bucketName, fileName, fileExtension, roomId);
    }
}
