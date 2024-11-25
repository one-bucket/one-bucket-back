package com.onebucket.domain.boardManage.service.postService;


import com.onebucket.domain.boardManage.dto.postDto.PostDto;
import com.onebucket.domain.boardManage.dto.postDto.PostKeyDto;
import org.springframework.data.domain.Page;

/**
 * <br>package name   : com.onebucket.domain.boardManage.service
 * <br>file name      : PostService
 * <br>date           : 2024-07-18
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
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-07-18        jack8              init create
 * </pre>
 */
public interface PostService extends BasePostService{

    Page<PostDto.InternalThumbnail> getLikePost(PostKeyDto.UserPage dto);
}
