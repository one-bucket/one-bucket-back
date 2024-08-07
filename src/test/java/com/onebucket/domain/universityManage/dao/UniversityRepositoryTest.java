package com.onebucket.domain.universityManage.dao;

import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.universityManage.domain.University;
import com.onebucket.global.exceptionManage.customException.universityManageException.UniversityException;
import com.onebucket.global.exceptionManage.errorCode.UniversityErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * <br>package name   : com.onebucket.domain.universityManage.dao
 * <br>file name      : UniversityRepositoryTest
 * <br>date           : 2024-06-30
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
 * 2024-06-30        SeungHoon              init create
 * </pre>
 */
@DataJpaTest
class UniversityRepositoryTest {

    private University university;

    @Autowired
    private UniversityRepository universityRepository;

    @BeforeEach
    void setUp() {
        university = University.builder()
                .name("홍익대학교")
                .address("서울시 마포구")
                .email("@hongik.ac.kr")
                .build();
        universityRepository.save(university);
    }

    @Test
    @DisplayName("학교 찾기 성공")
    void getUniversityByName_success() {
        University university = universityRepository.findByName("홍익대학교")
                .orElseThrow(()-> new UniversityException(UniversityErrorCode.NOT_EXIST_UNIVERSITY));

        assertThat(university.getName()).isEqualTo("홍익대학교");
        assertThat(university.getAddress()).isEqualTo("서울시 마포구");
        assertThat(university.getEmail()).isEqualTo("@hongik.ac.kr");
    }

    @Test
    @DisplayName("학교 찾기 실패 - 해당하는 이름이 없음.")
    void getUniversityByName_fail() {
        assertThat(universityRepository.findByName("서울대학교")).isEqualTo(Optional.empty());
    }
}