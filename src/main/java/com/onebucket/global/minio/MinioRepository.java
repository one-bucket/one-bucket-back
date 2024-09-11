package com.onebucket.global.minio;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * <br>package name   : com.onebucket.global.minio
 * <br>file name      : MinioRepository
 * <br>date           : 2024-07-02
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
 * 2024-07-02        jack8              init create
 * </pre>
 */

@Repository
@RequiredArgsConstructor
public class MinioRepository {

     private final MinioClient minioClient;

     public String uploadFile(MultipartFile multipartFile, MinioSaveInfoDto dto) {
         try {
             boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder()
                     .bucket(dto.getBucketName()).build());
             if(!isExist) {
                 minioClient.makeBucket(MakeBucketArgs.builder().bucket(dto.getBucketName()).build());
                 minioClient.setBucketPolicy(
                         SetBucketPolicyArgs.builder()
                                 .bucket(dto.getBucketName())
                                 .config("public")
                                 .build()
                 );
             }

             try(InputStream inputStream = multipartFile.getInputStream()) {
                 minioClient.putObject(
                         PutObjectArgs.builder()
                                 .bucket(dto.getBucketName())
                                 .object(dto.getFileName() + "." + dto.getFileExtension())
                                 .stream(inputStream, multipartFile.getSize(), -1)
                                 .contentType(multipartFile.getContentType())
                                 .build()
                 );
             }


             return dto.getBucketName() + "/" + dto.getFileName() + "." + dto.getFileExtension();

         } catch(Exception e) {
             // TODO: make this as custom.
             throw new RuntimeException("Error occur : " + e.getMessage());
         }
     }

     public byte[] getFile(MinioSaveInfoDto dto) {
         try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
             try (InputStream stream = minioClient.getObject(
                     GetObjectArgs.builder()
                             .bucket(dto.getBucketName())
                             .object(dto.getFileName() + "." + dto.getFileExtension())
                             .build())) {
                 byte[] buffer = new byte[1024];
                 int byteRead;
                 while((byteRead = stream.read(buffer)) != -1) {
                     baos.write(buffer, 0, byteRead);
                 }
                 return baos.toByteArray();
             }
         }catch(MinioException | IOException e) {
             throw new RuntimeException("Error occurred while fetching files:" + e.getMessage());
         } catch(Exception e) {
             throw new RuntimeException("Unknown Exception in MinioRepository.class : " + e.getMessage());
         }
     }

     public void deleteFile(MinioSaveInfoDto dto) {
         try{
             minioClient.removeObject(
                     RemoveObjectArgs.builder()
                             .bucket(dto.getBucketName())
                             .object(dto.getFileName() + "." + dto.getFileExtension())
                             .build()
             );
         } catch(MinioException | IOException e) {
             throw new RuntimeException("Error occurred while deleting file: " + e.getMessage());
         }catch (Exception e) {
             throw new RuntimeException("Unknown Exception in MinioRepository.class : " + e.getMessage());
         }
     }

    public void deleteAllFilesInRoom(MinioDeleteInfoDto dto) {
        try {
            // "chat/{roomId}/" 경로에 있는 모든 파일 가져오기
            String folderPath = "chat/" + dto.roomId() + "/";
            Iterable<Result<Item>> files = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(dto.bucketName())
                            .prefix(folderPath) // 특정 폴더에 있는 모든 파일을 가져옴
                            .recursive(true) // 모든 하위 폴더도 포함
                            .build());

            // 각 파일을 삭제
            for (Result<Item> file : files) {
                String fileName = file.get().objectName(); // 파일 이름 가져오기
                minioClient.removeObject(RemoveObjectArgs.builder()
                        .bucket(dto.bucketName())
                        .object(fileName)
                        .build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete files in room: " + dto.roomId(), e);
        }
    }

     public String getUrl(MinioSaveInfoDto dto) {
         try {
             return minioClient.getPresignedObjectUrl(
                     GetPresignedObjectUrlArgs.builder()
                             .method(Method.GET)
                             .bucket(dto.getBucketName())
                             .object(dto.getFileName() + "." + dto.getFileExtension())
                             .expiry(1, TimeUnit.HOURS)
                             .build());
         } catch (Exception e) {
             throw new RuntimeException("Error occur : " + e.getMessage());
         }
     }


}
