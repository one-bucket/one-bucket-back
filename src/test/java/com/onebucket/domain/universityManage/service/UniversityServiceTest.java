package com.onebucket.domain.universityManage.service;


import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;


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
//    @Mock
//    private UniversityRepository universityRepository;
//
//    @Mock
//    private UniversityEmailValidator universityEmailValidator;
//
//    @Mock
//    private University university;
//
//    @InjectMocks
//    private UniversityServiceImpl universityService;
//
//    private UniversityDto createUniversityDto() {
//        return UniversityDto.builder()
//                .name("홍익대학교")
//                .address("서울시 마포구 상수동")
//                .email("@hongik.ac.kr")
//                .build();
//    }
//
//    private University createUniversity() {
//        return University.builder()
//                .id(-1L)
//                .name("홍익대학교")
//                .address("서울시 마포구 상수동")
//                .email("@hongik.ac.kr")
//                .build();
//    }
//
////    /**
////     * 실제 DB에 저장하는 것이 아니기 때문에 id가 자동으로 생성되지 않는듯. 그래서 저렇게 assertThat을 작성하면 안됨.
////     * UniversityService에서는 id를 Builder에서 삽입하지 않음. 그래서 id==null 이 된다. 추후 다시 작성해보자.
////     */
////    @Test
////    @DisplayName("대학교 만들기 성공")
////    void createUniversity_success() {
////        university = createUniversity();
////        CreateUniversityDto dto = createUniversityDto();
////        when(universityEmailValidator.isValidUniversityEmail(dto.getName(), dto.getEmail())).thenReturn(true);
////        doReturn(University.builder().build()).when(universityRepository).save(any(University.class));
////        doReturn(-1L).when(university.getId());
////        // 대학 생성 서비스 호출
////        Long result = universityService.createUniversity(dto);
////        // 결과 검증
////        assertThat(result).isNotNull();
////    }
//
//
//    @Test
//    @DisplayName("대학교 찾기 실패 - 해당 이름을 가진 대학교가 없음")
//    void FindUniversityById_fail_notExistUniversity() {
//
//        doReturn(Optional.empty()).when(universityRepository).findById(-1L);
//
//        final UniversityException result = assertThrows(UniversityException.class,
//                ()->universityService.getUniversity(-1L));
//
//        assertThat(result.getErrorCode()).isEqualTo(UniversityErrorCode.NOT_EXIST_UNIVERSITY);
//    }
//
//    @Test
//    @DisplayName("대학교 찾기 성공")
//    void FindUniversityByName_success() {
//        doReturn(Optional.of(createUniversity())).when(universityRepository).findById(-1L);
//
//        final ResponseUniversityDto result = universityService.getUniversity(-1L);
//
//        assertThat(result.getName()).isEqualTo("홍익대학교");
//        assertThat(result.getAddress()).isEqualTo("서울시 마포구 상수동");
//    }
}