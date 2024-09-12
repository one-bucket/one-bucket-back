package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dao.PostRepository;
import com.onebucket.domain.boardManage.entity.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

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

    @Scheduled(cron = "0 */3 * * * *")
    @Transactional
    public void syncLikesFromRedisToPost() {
        Set<String> keys = stringRedisTemplate.keys("post:likes:*");

        if(keys != null && !keys.isEmpty()) {
            for(String key : keys) {
                stringRedisTemplate.watch(key);

                String valueStr = stringRedisTemplate.opsForValue().get(key);
                if(valueStr == null) {
                    stringRedisTemplate.unwatch();
                    continue;
                }

                int likesToAdd = Integer.parseInt(valueStr);
                String postIdStr = key.split(":")[2];
                Long postId = Long.parseLong(postIdStr);

                try {
                    Post post = postRepository.findById(postId).orElse(null);
                    if(post == null) {
                        stringRedisTemplate.unwatch();
                        continue;
                    }

                    post.setLikes(post.getLikes() + likesToAdd);
                    postRepository.save(post);

                    stringRedisTemplate.multi();
                    stringRedisTemplate.delete(key);

                    List<Object> result = stringRedisTemplate.exec();
                    if(result.isEmpty()) {
                        stringRedisTemplate.unwatch();
                        throw new RuntimeException();
                    }
                } catch (Exception e) {
                    stringRedisTemplate.discard();
                    stringRedisTemplate.unwatch();
                }
            }
        }
    }
}
