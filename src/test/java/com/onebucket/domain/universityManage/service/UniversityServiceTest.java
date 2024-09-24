package com.onebucket.domain.universityManage.service;


import com.onebucket.domain.universityManage.dao.UniversityRepository;
import com.onebucket.domain.universityManage.domain.University;
import com.onebucket.domain.universityManage.dto.university.UniversityDto;
import com.onebucket.global.exceptionManage.customException.universityManageException.UniversityException;
import com.onebucket.global.exceptionManage.errorCode.UniversityErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


/**
 * <br>package name   : com.onebucket.domain.universityManage.service
 * <br>file name      : UniversityServiceTest
 * <br>date           : 2024-07-05
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
 * 2024-07-05        SeungHoon              init create
 * </pre>
 */
@ExtendWith(MockitoExtension.class)
class UniversityServiceTest {
    @Mock
    private UniversityRepository universityRepository;

    @InjectMocks
    private UniversityServiceImpl universityService;

    private UniversityDto createUniversityDto() {
        return UniversityDto.builder()
                .name("홍익대학교")
                .address("서울시 마포구 상수동")
                .email("@hongik.ac.kr")
                .build();
    }

    private University createUniversity() {
        return University.builder()
                .id(-1L)
                .name("홍익대학교")
                .address("서울시 마포구 상수동")
                .email("@hongik.ac.kr")
                .build();
    }

    @Test
    @DisplayName("대학교 만들기 성공")
    void createUniversity_success() {
        UniversityDto dto = createUniversityDto();
        // 대학 생성 서비스 호출
        universityService.createUniversity(dto);
        // 결과 검증
        verify(universityRepository, times(1)).save(any(University.class));
    }

    @Test
    @DisplayName("대학교 찾기 성공")
    void FindUniversityByName_success() {
        universityRepository.save(createUniversity());
        doReturn(Optional.of(createUniversity())).when(universityRepository).findByName("홍익대학교");

        final UniversityDto result = universityService.getUniversity("홍익대학교");

        assertThat(result.getName()).isEqualTo("홍익대학교");
        assertThat(result.getAddress()).isEqualTo("서울시 마포구 상수동");
    }

    @Test
    @DisplayName("대학교 찾기 실패 - 해당 이름을 가진 대학교가 없음")
    void FindUniversityById_fail_notExistUniversity() {
        universityRepository.save(createUniversity());
        doReturn(Optional.empty()).when(universityRepository).findByName("홍익대학교");

        final UniversityException result = assertThrows(UniversityException.class,
                ()->universityService.getUniversity("홍익대학교"));

        assertThat(result.getErrorCode()).isEqualTo(UniversityErrorCode.NOT_EXIST_UNIVERSITY);
    }
}