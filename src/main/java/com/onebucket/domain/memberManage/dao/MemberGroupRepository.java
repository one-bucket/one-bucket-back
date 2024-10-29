package com.onebucket.domain.memberManage.dao;

import com.onebucket.domain.memberManage.domain.MemberGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <br>package name   : com.onebucket.domain.memberManage.dao
 * <br>file name      : MemberGroupRepository
 * <br>date           : 10/29/24
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
public interface MemberGroupRepository extends JpaRepository<MemberGroup, Long> {
}
