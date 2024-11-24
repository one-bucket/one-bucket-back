package com.onebucket.domain.PushMessageManage.JpaDao;

import com.onebucket.domain.PushMessageManage.Entity.DeviceToken;
import com.onebucket.domain.memberManage.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * <br>package name   : com.onebucket.domain.PushMessageManage.dao
 * <br>file name      : DeviceTokenRepository
 * <br>date           : 11/5/24
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 */
@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceToken, String> {
    Optional<DeviceToken> findByMember(Member member);
    Optional<DeviceToken> findByMemberId(Long id);
}
