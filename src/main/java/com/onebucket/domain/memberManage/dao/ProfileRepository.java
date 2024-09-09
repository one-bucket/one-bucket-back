package com.onebucket.domain.memberManage.dao;

import com.onebucket.domain.memberManage.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * <br>package name   : com.onebucket.domain.memberManage.dao
 * <br>file name      : ProfileRepository
 * <br>date           : 2024-07-02
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 */

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    boolean existsById(Long id);

    @Query("SELECT p FROM Profile p JOIN FETCH p.wallet WHERE p.id = :id")
    Optional<Profile> findProfileWithWalletById(@Param("id") Long id);

}
