package com.onebucket.global.minio;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.embedded.TomcatVirtualThreadsWebServerFactoryCustomizer;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
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

    public String uploadFile(MultipartFile multipartFile, MinioInfoDto dto) {
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

    public String uploadFile(String base64Image, MinioInfoDto dto) {
        try {
            // 버킷이 존재하는지 확인하고 없으면 생성
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(dto.getBucketName()).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(dto.getBucketName()).build());
                minioClient.setBucketPolicy(
                        SetBucketPolicyArgs.builder()
                                .bucket(dto.getBucketName())
                                .config("public")
                                .build()
                );
            }

            // Base64 문자열을 디코딩하여 InputStream으로 변환
            String[] parts = base64Image.split(",");
            byte[] imageBytes = Base64.getDecoder().decode(parts[1]); // "data:image/png;base64," 부분 이후만 사용
            try (InputStream inputStream = new ByteArrayInputStream(imageBytes)) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(dto.getBucketName())
                                .object(dto.getFileName() + "." + dto.getFileExtension())
                                .stream(inputStream, imageBytes.length, -1)
                                .contentType("image/" + dto.getFileExtension()) // 적절한 Content-Type 설정
                                .build()
                );
            }

            return dto.getBucketName() + "/" + dto.getFileName() + "." + dto.getFileExtension();

        } catch (Exception e) {
            throw new RuntimeException("Error occurred : " + e.getMessage());
        }
    }

     public byte[] getFile(MinioInfoDto dto) {
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

     public void deleteFile(MinioInfoDto dto) {
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

     public void deleteDirectory(MinioInfoDto dto) {
         try {
             Iterable<Result<Item>> objects = minioClient.listObjects(
                     ListObjectsArgs.builder()
                             .bucket(dto.getBucketName())
                             .prefix(dto.getFileName())
                             .recursive(true)
                             .build()
             );
             for(Result<Item> result : objects) {
                 Item item = result.get();
                 minioClient.removeObject(
                         RemoveObjectArgs.builder()
                                 .bucket(dto.getBucketName())
                                 .object(item.objectName())
                                 .build()
                 );
             }
         } catch (MinioException | IOException e) {
             throw new RuntimeException("Error Occurred while deleting directory");
         } catch (Exception e) {
             throw new RuntimeException("Unknown exception in MinioRepository.class");
         }
     }

     public String getUrl(MinioInfoDto dto) {
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
