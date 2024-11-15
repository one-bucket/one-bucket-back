package com.onebucket.domain.announcementManage.dao;

import com.onebucket.domain.announcementManage.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <br>package name   : com.onebucket.domain.announcementManage.dao
 * <br>file name      : AnnouncementRepository
 * <br>date           : 11/6/24
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
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
}
