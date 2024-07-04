package com.onebucket.test;


import com.onebucket.global.minio.MinioRepository;
import com.onebucket.global.minio.MinioSaveInfoDto;
import com.onebucket.global.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final SecurityUtils securityUtils;
    private final MinioRepository minioRepository;

    @GetMapping("/test/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/member-info")
    public String memberInfo() {
        return securityUtils.getCurrentUsername();
    }

    @PostMapping("/test/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file")MultipartFile file) {
        MinioSaveInfoDto dto = MinioSaveInfoDto.builder()
                .fileName("testFile")
                .fileExtension("png")
                .bucketName("test-bucket")
                .build();

        String objectName = minioRepository.uploadFile(file,dto);
        return ResponseEntity.ok("success upload : " + objectName);
    }

}
