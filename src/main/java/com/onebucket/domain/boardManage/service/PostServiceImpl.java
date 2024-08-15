package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dao.BoardRepository;
import com.onebucket.domain.boardManage.dao.PostRepository;
import com.onebucket.domain.boardManage.dto.internal.CreatePostDto;
import com.onebucket.domain.boardManage.dto.internal.DeletePostDto;
import com.onebucket.domain.boardManage.dto.internal.GetBoardDto;
import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.boardManage.entity.Comment;
import com.onebucket.domain.boardManage.entity.post.Post;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.global.exceptionManage.customException.boardManageException.BoardManageException;
import com.onebucket.global.exceptionManage.customException.boardManageException.UserBoardException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.customException.universityManageException.UniversityException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.BoardErrorCode;
import com.onebucket.global.exceptionManage.errorCode.UniversityErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public Long createPost(CreatePostDto dto) {

        Board board = findBoard(dto.getBoardId());
        Long boardUnivId = board.getUniversity().getId();

        Member member = findMember(dto.getUsername());
        Long memberUnivId = member.getUniversity().getId();

        if(memberUnivId == null) {
            throw new UniversityException(UniversityErrorCode.NOT_EXIST_UNIVERSITY,
                    "expected university exist, but was not");
        }

        if(memberUnivId.equals(boardUnivId)) {

            Post post = Post.builder()
                    .board(board)
                    .title(dto.getTitle())
                    .text(dto.getText())
                    .author(member)
                    .build();

            return postRepository.save(post).getId();
        } else {
            throw new UniversityException(UniversityErrorCode.NOT_EXIST_UNIVERSITY,
                     "expected correct university, but was not");
        }
    }

    @Override
    @Transactional
    public void deletePost(DeletePostDto dto) {
        Post findPost = findPost(dto.getId());

        String author = findPost.getAuthor().getUsername();

        if(author.equals(dto.getUsername())) {
            postRepository.delete(findPost);
        } else {
            throw new AuthenticationException(AuthenticationErrorCode.UNAUTHORIZED_ACCESS,
                    "you are not allowed to edit this post");
        }
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
    public Page<Post> getPostsByBoard(GetBoardDto dto) {
        return postRepository.findByBoardId(dto.getBoardId(), dto.getPageable());
    }

    private Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() ->
                new BoardManageException(BoardErrorCode.UNKNOWN_BOARD));
    }

    private Member findMember(String username) {
        return memberRepository.findByUsername(username).orElseThrow(() ->
                new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new BoardManageException(BoardErrorCode.UNKNOWN_POST));
    }

}
