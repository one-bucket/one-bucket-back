package com.onebucket.domain.universityManage.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.onebucket.domain.universityManage.domain.University;

import java.util.Optional;

/**
 * <br>package name   : com.onebucket.domain.universityManage.dao
 * <br>file name      : UniversityRepository
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
@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
}
