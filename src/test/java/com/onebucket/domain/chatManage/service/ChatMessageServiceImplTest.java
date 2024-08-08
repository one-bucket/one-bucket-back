package com.onebucket.domain.chatManage.service;

import com.onebucket.global.exceptionManage.customException.chatManageException.ChatManageException;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import com.onebucket.global.minio.MinioRepository;
import com.onebucket.global.minio.MinioSaveInfoDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * <br>package name   : com.onebucket.domain.chatManage.service
 * <br>file name      : ChatMessageServiceImplTest
 * <br>date           : 2024-08-07
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
 * 2024-08-07        SeungHoon              init create
 * </pre>
 */
@ExtendWith(MockitoExtension.class)
class ChatMessageServiceImplTest {

    @Mock
    private MinioRepository minioRepository;

    @InjectMocks
    private ChatMessageServiceImpl chatMessageService;

    @Test
    @DisplayName("채팅 이미지 업로드 성공")
    void uploadChatImage_success() {
        // given
        String username = "testuser";
        String endpointUrl = "https://minio-endpoint.com";
        String bucketName = "bucket-name";

        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", "some-image-content".getBytes());
        String expectedAddress = bucketName + "/" + "chat/" + username + "/test.png";
        String expectedUrl = endpointUrl + "/" + expectedAddress;
        ReflectionTestUtils.setField(chatMessageService, "endpointUrl", endpointUrl);

        // when
        when(minioRepository.uploadFile(any(MultipartFile.class), any(MinioSaveInfoDto.class)))
                .thenReturn(expectedAddress);

        String actualUrl = chatMessageService.uploadChatImage(file, username);

        // then
        assertEquals(expectedUrl, actualUrl);
        verify(minioRepository, times(1)).uploadFile(eq(file), any(MinioSaveInfoDto.class));
    }

    @Test
    @DisplayName("채팅 이미지 업로드 실패 - minio save 실패")
    void uploadChatImage_fail() {
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", "some-image-content".getBytes());
        doThrow(new RuntimeException("error occur : message")).when(minioRepository)
                .uploadFile(any(MultipartFile.class), any(MinioSaveInfoDto.class));

        assertThatThrownBy(() -> chatMessageService.uploadChatImage(file,"testuser"))
                .isInstanceOf(ChatManageException.class)
                .extracting("errorCode")
                .isEqualTo(ChatErrorCode.CHAT_IMAGE_ERROR);
    }
}