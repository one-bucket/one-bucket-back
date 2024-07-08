package com.onebucket.domain.universityManage.dao;

import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.universityManage.domain.University;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
    }

    @Test
    @DisplayName("학교 찾기 성공 - id로 찾기")
    void getUniversityById() {
        universityRepository.save(university);
        boolean present = universityRepository.findById(university.getId()).isPresent();
        assertThat(present).isTrue();
    }

    @Test
    @DisplayName("학교 찾기  - 없는 학교임")
    void CantGetUniversityById() {
        universityRepository.save(university);
        boolean present = universityRepository.findById(-1L).isPresent();
        assertThat(present).isFalse();
    }
}