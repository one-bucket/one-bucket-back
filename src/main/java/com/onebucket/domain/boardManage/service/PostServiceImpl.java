package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dao.BoardRepository;
import com.onebucket.domain.boardManage.dao.PostRepository;
import com.onebucket.domain.boardManage.dto.CreatePostDto;
import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.boardManage.entity.Comment;
import com.onebucket.domain.boardManage.entity.post.Post;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.global.exceptionManage.customException.boardManageException.BoardManageException;
import com.onebucket.global.exceptionManage.customException.boardManageException.UserBoardException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.BoardErrorCode;
import lombok.RequiredArgsConstructor;
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
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;


    @Override
    @Transactional
    public void createPost(String username, CreatePostDto dto) {

        Board board = boardRepository.findById(Long.parseLong(dto.getBoard())).orElseThrow(() ->
                new BoardManageException(BoardErrorCode.UNKNOWN_BOARD));
        Member member = memberRepository.findByUsername(username).orElseThrow(() ->
                new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));


        Post post = Post.builder()
                .board(board)
                .title(dto.getTitle())
                .text(dto.getText())
                .author(member)
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
