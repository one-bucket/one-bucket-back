package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dao.BoardRepository;
import com.onebucket.domain.boardManage.dao.CommentRepository;
import com.onebucket.domain.boardManage.dao.PostRepository;
import com.onebucket.domain.boardManage.dto.internal.board.GetBoardDto;
import com.onebucket.domain.boardManage.dto.internal.comment.CreateCommentDto;
import com.onebucket.domain.boardManage.dto.internal.comment.GetCommentDto;
import com.onebucket.domain.boardManage.dto.internal.post.*;
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
import com.onebucket.global.utils.RandomStringUtils;
import com.onebucket.global.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


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
    private final SecurityUtils securityUtils;
    private final RandomStringUtils randomStringUtils;
    private final CommentRepository commentRepository;


    @Override
    @Transactional
    public Long createPost(CreatePostDto dto) {
        Long univId = dto.getUnivId();
        Long boardId = dto.getBoardId();
        Member member = securityUtils.getMember(dto.getUsername());
        Board board = findBoard(boardId);

        securityUtils.isUserUniversityMatchingBoard(univId, board);

        Long postId = randomStringUtils.generateId(univId);

        Post post = Post.builder()
                .id(postId)
                .author(member)
                .title(dto.getTitle())
                .text(dto.getText())
                .board(board)
                .build();
        postRepository.save(post);

        return postId;
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
    public void addCommentToPost(CreateCommentDto dto) {
        Post post = postRepository.findById(dto.getPostId()).orElseThrow(() ->
                new UserBoardException(BoardErrorCode.UNKNOWN_POST));

        Member member = memberRepository.findByUsername(dto.getUsername()).orElseThrow(() ->
                new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));



        Long id = randomStringUtils.generateId(1L);
        Comment comment = Comment.builder()
                .id(id)
                .author(member)
                .text(dto.getText())
                .isModified(false)
                .build();

        // If parent comment exists, handle it first
        if (dto.getParentCommentId() != null) {
            Comment parentComment = post.getComments().stream()
                    .filter(findComment -> findComment.getId().equals(dto.getParentCommentId()))
                    .findFirst()
                    .orElseThrow(() -> new UserBoardException(BoardErrorCode.UNKNOWN_COMMENT));

            comment.setParentComment(parentComment);
            parentComment.addReply(comment);
            commentRepository.save(parentComment); // Save the parent with the new reply

        } else {
            post.addComment(comment);
            postRepository.save(post);
        }

        // Save the new comment (only if it's not already managed by Hibernate)
        commentRepository.save(comment);
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
    public Page<PostThumbnailDto> getPostsByBoard(GetBoardDto dto) {
        return postRepository.findByBoardId(dto.getBoardId(), dto.getPageable())
                .map(post -> {

                    return PostThumbnailDto.builder()
                            .postId(post.getId())
                            .boardId(post.getBoard().getId())
                            .authorNickname(post.getAuthor().getNickname())
                            .title(post.getTitle())
                            .text(post.getText())
                            .createdDate(post.getCreatedDate())
                            .modifiedDate(post.getModifiedDate())
                            .build();

                });
    }

    @Override
    @Transactional(readOnly = true)
    public PostInfoDto getPost(GetPostDto dto) {
        // Post를 조회
        Post post = postRepository.findById(dto.getPostId()).orElseThrow(() ->
                new BoardManageException(BoardErrorCode.UNKNOWN_POST));

        Long boardId = post.getBoardId();

        // 사용자의 접근 권한 확인
        securityUtils.isUserUniversityMatchingBoard(dto.getUsername(), boardId);

        // 대댓글을 제외한 직접적인 댓글만 조회
        List<GetCommentDto> comments = post.getComments().stream()
                .filter(comment -> comment.getParentComment() == null)  // 부모 댓글이 없는(즉, 직접적인 댓글)만 필터링
                .map(this::convertToGetCommentDto)
                .collect(Collectors.toList());

        // PostInfoDto를 반환
        return PostInfoDto.builder()
                .boardId(post.getBoardId())
                .authorNickname(post.getAuthor().getNickname())
                .createdDate(post.getCreatedDate())
                .modifiedDate(post.getModifiedDate())
                .comments(comments)
                .build();
    }




    private Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() ->
                new BoardManageException(BoardErrorCode.UNKNOWN_BOARD));
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new BoardManageException(BoardErrorCode.UNKNOWN_POST));
    }

    private GetCommentDto convertToGetCommentDto(Comment comment) {
        List<GetCommentDto> replies = comment.getReplies().stream()
                .map(this::convertToGetCommentDto)
                .toList();

        return GetCommentDto.builder()
                .commentId(comment.getId())
                .postId(comment.getPost().getId())
                .authorNickname(comment.getAuthor().getNickname())
                .text(comment.getText())
                .modifiedDate(comment.getModifiedDate())
                .replies(replies)
                .build();
    }

}
