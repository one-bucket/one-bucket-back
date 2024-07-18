package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dao.CommentRepository;
import com.onebucket.domain.boardManage.dao.PostRepository;
import com.onebucket.domain.boardManage.dto.CreatePostDto;
import com.onebucket.domain.boardManage.entity.Comment;
import com.onebucket.domain.boardManage.entity.post.Post;
import com.onebucket.global.exceptionManage.customException.boardManageException.UserBoardException;
import com.onebucket.global.exceptionManage.errorCode.BoardErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.output.AppendableOutputStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <br>package name   : com.onebucket.domain.boardManage.service
 * <br>file name      : PostServiceImpl
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

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;


    @Override
    @Transactional
    public void createPost(CreatePostDto createPostDto) {
        Post post = Post.builder()
                .board(createPostDto.getBoard())
                .title(createPostDto.getTitle())
                .text(createPostDto.getText())
                .author(createPostDto.getAuthor())
                .build();

        postRepository.save(post);
    }

    @Override
    @Transactional
    public void addCommentToPost(Long postId, Comment comment) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new UserBoardException(BoardErrorCode.UNKNOWN_POST));
        post.addComment(comment);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void deleteCommentFromPost(Long postId, Comment comment) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new UserBoardException(BoardErrorCode.UNKNOWN_POST));
        post.deleteComment(comment);
        postRepository.save(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

}
