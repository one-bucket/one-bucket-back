package com.onebucket.domain.universityManage.service;

import com.onebucket.domain.universityManage.dao.UniversityRepository;
import com.onebucket.domain.universityManage.domain.University;
import com.onebucket.domain.universityManage.dto.ResponseUniversityDto;
import com.onebucket.global.exceptionManage.customException.universityManageException.UniversityException;
import com.onebucket.global.exceptionManage.errorCode.UniversityErrorCode;
import com.onebucket.global.utils.UniversityEmailValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

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

    @Mock
    private University university;

    @Mock
    private UniversityEmailValidator universityEmailValidator;

    @InjectMocks
    private UniversityServiceImpl universityService;

    private University getDto1() {
        return University.builder()
                .id(-1L)
                .name("홍익대학교")
                .address("서울시 마포구 상수동")
                .email("@hongik.ac.kr")
                .build();
    }

    // 새로운 getDto2() 메서드
    private University getDto2() {
        return University.builder()
                .id(-2L)
                .name("서울대학교")
                .address("서울시 관악구 관악로")
                .email("@snu.ac.kr")
                .build();
    }

    @Test
    @DisplayName("대학교 찾기 실패 - 해당 이름을 가진 대학교가 없음")
    void FindUniversityByName_fail_notExistUniversity() {

        doReturn(Optional.empty()).when(universityRepository).findByName("연세대학교");

        final UniversityException result = assertThrows(UniversityException.class,
                ()->universityService.getUniversityByName("연세대학교"));

        assertThat(result.getErrorCode()).isEqualTo(UniversityErrorCode.NOT_EXIST_UNIVERSITY);
    }

    @Test
    @DisplayName("대학교 찾기 성공")
    void FindUniversityByName_success_existUniversity() {
        doReturn(Optional.of(getDto1())).when(universityRepository).findByName("홍익대학교");

        final ResponseUniversityDto result = universityService.getUniversityByName("홍익대학교");

        assertThat(result.getName()).isEqualTo("홍익대학교");
        assertThat(result.getAddress()).isEqualTo("서울시 마포구 상수동");
    }
}