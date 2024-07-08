package com.onebucket.domain.memberManage.service;

import com.onebucket.domain.memberManage.dao.ProfileRepository;
import com.onebucket.global.minio.MinioRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <br>package name   : com.onebucket.domain.memberManage.service
 * <br>file name      : ProfileServiceTest
 * <br>date           : 2024-07-06
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
 * 2024-07-06        jack8              init create
 * </pre>
 */

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private MinioRepository minioRepository;

    @InjectMocks
    private ProfileServiceImpl profileService;

}