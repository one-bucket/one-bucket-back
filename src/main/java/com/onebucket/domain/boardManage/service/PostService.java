package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dto.internal.board.GetBoardDto;
import com.onebucket.domain.boardManage.dto.internal.comment.CreateCommentDto;
import com.onebucket.domain.boardManage.dto.internal.post.*;
import com.onebucket.domain.boardManage.entity.Comment;
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
public interface PostService {
    Long createPost(CreatePostDto dto);
    void deletePost(DeletePostDto dto);

    void addCommentToPost(CreateCommentDto dto);

    void deleteCommentFromPost(Long postId, Comment comment);

    Page<PostThumbnailDto> getPostsByBoard(GetBoardDto dto);

    PostInfoDto getPost(GetPostDto dto);

    void increaseViewCount(PostAuthorDto dto);
}
