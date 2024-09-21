package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dto.internal.board.GetBoardDto;
import com.onebucket.domain.boardManage.dto.internal.comment.CreateCommentDto;
import com.onebucket.domain.boardManage.dto.internal.post.*;
import com.onebucket.domain.boardManage.entity.Comment;
import org.springframework.data.domain.Page;

/**
 * <br>package name   : com.onebucket.domain.boardManage.service
 * <br>file name      : BasePostService
 * <br>date           : 2024-09-21
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
public interface BasePostService {

    <D extends CreatePostDto> Long createPost(D dto);
    void deletePost(DeletePostDto deletePostDto);
    void addCommentToPost(CreateCommentDto dto);
    void deleteCommentFromPost(Long postId, Comment comment);
    Page<PostThumbnailDto> getPostsByBoard(GetBoardDto dto);
    PostInfoDto getPost(GetPostDto dto);
    void increaseViewCount(PostAuthorDto dto);
    void increaseLikesCount(PostAuthorDto dto);
    void decreaseLikesCount(PostAuthorDto dto);
    Long getCommentCount(Long postId);
}
