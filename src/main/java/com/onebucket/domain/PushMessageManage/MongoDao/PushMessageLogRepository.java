package com.onebucket.domain.PushMessageManage.MongoDao;

import com.onebucket.domain.PushMessageManage.Entity.PushMessageLog.PushMessageLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * <br>package name   : com.onebucket.domain.PushMessageManage.MongoDao
 * <br>file name      : PushMessageLogRepository
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
public interface PushMessageLogRepository extends MongoRepository<PushMessageLog, Long> {

}
