package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dao.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.service
 * <br>file name      : SyncDataScheduledService
 * <br>date           : 2024-09-12
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

@Service
@RequiredArgsConstructor
public class SyncDataScheduledService {

    private final PostRepository postRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisScript<List> syncLikeScript;

    @Scheduled(cron = "0 */3 * * * *")
    @Transactional
    public void syncLikesFromRedisToPost() {

        List<List<String>> result = stringRedisTemplate.execute(syncLikeScript, List.of());

        if(result != null ) {
            for(List<String> entry : result) {
                Long postId = Long.valueOf(entry.get(0));
                Long likesToAdd = Long.valueOf(entry.get(1));

                if(likesToAdd == 0) {
                    continue;
                }
                postRepository.updateLikes(postId, likesToAdd);
            }
        }

    }
}
