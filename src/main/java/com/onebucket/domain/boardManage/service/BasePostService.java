package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dto.internal.comment.CreateCommentDto;
import com.onebucket.domain.boardManage.dto.internal.post.*;
import com.onebucket.domain.boardManage.dto.parents.PostDto;
import com.onebucket.domain.boardManage.dto.parents.ValueDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

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

    <D extends PostDto.Create> Long createPost(D dto);
    void deletePost(ValueDto.FindPost dto);
    void addCommentToPost(CreateCommentDto dto);
    void deleteCommentFromPost(ValueDto.FindComment dto);
    Page<PostDto.Thumbnail> getPostsByBoard(ValueDto.PageablePost dto);
    PostDto.Info getPost(ValueDto.GetPost dto);
    Page<PostDto.Thumbnail> getPostByAuthorId(ValueDto.AuthorPageablePost dto);
    void increaseViewCount(ValueDto.FindPost dto);
    void increaseLikesCount(ValueDto.FindPost dto);
    void decreaseLikesCount(ValueDto.FindPost dto);
    Long getCommentCount(Long postId);
    Long getLikesInRedis(Long postId);
    boolean isUserLikesPost(ValueDto.FindPost dto);
    void saveImage(MultipartFile multipartFile, SaveImageDto dto);
    Page<PostDto.Thumbnail> getSearchResult(ValueDto.SearchPageablePost dto);
}
