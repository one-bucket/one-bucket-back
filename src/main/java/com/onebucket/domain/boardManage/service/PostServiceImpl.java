package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dao.BoardRepository;
import com.onebucket.domain.boardManage.dao.CommentRepository;
import com.onebucket.domain.boardManage.dao.LikesMapRepository;
import com.onebucket.domain.boardManage.dao.PostRepository;
import com.onebucket.domain.boardManage.dto.internal.board.GetBoardDto;
import com.onebucket.domain.boardManage.dto.internal.comment.CreateCommentDto;
import com.onebucket.domain.boardManage.dto.internal.comment.GetCommentDto;
import com.onebucket.domain.boardManage.dto.internal.post.*;
import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.boardManage.entity.Comment;
import com.onebucket.domain.boardManage.entity.LikesMap;
import com.onebucket.domain.boardManage.entity.LikesMapId;
import com.onebucket.domain.boardManage.entity.post.Post;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.global.exceptionManage.customException.boardManageException.BoardManageException;
import com.onebucket.global.exceptionManage.customException.boardManageException.UserBoardException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.BoardErrorCode;
import com.onebucket.global.redis.RedisRepository;
import com.onebucket.global.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
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
 * post 에 대한 service layer. CRUD 및 조회수 증가를 포함하고 있다.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * @see PostRepository
 * @see BoardRepository
 * @see MemberRepository
 * @see SecurityUtils
 * @see CommentRepository
 * @see RedisRepository
 */

@RequiredArgsConstructor
@Service

public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final SecurityUtils securityUtils;
    private final CommentRepository commentRepository;
    private final RedisRepository redisRepository;
    private final LikesMapRepository likesMapRepository;


    /**
     * 사용자의 정보와 게시글 정보를 받아 저장한다. 이 과정에서 해당 유저가 board에 접근할 권한이 있는지 확인한다.
     * @tested true
     * @param dto - username, board, title, text
     * @return id of saved post
     */
    @Override
    @Transactional
    public Long createPost(CreatePostDto dto) {
        Long univId = dto.getUnivId();
        Long boardId = dto.getBoardId();
        Member member = memberRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER,
                        "can't find member while create post"));
        Board board = findBoard(boardId);

        //권한 정보 확인
        if(!isUserMatchingBoard(univId, board)) {
            throw new AuthenticationException(AuthenticationErrorCode.INVALID_SUBMIT,
                    "can't access because of invalid university");
        }

        Post post = Post.builder()
                .author(member)
                .title(dto.getTitle())
                .text(dto.getText())
                .board(board)
                .build();

        Post savedPost = postRepository.save(post);

        return savedPost.getId();
    }

    /**
     * 사용자의 id와 post의 id를 받아서 post 의 author과 사용자와 일치하는지 확인한다. 이후 삭제한다.
     * 만약 사용자가 서비스 탈퇴로 존재하지 않으면 예외를 뱉는다.
     * @param dto - memberId, postId
     */
    @Override
    @Transactional
    public void deletePost(DeletePostDto dto) {
        Post findPost = findPost(dto.getId());


        Member author = findPost.getAuthor();
        if(author == null) {
            throw new BoardManageException(BoardErrorCode.I_AM_AN_APPLE_PIE, "maybe, author is null");
        }
        Long authorId = author.getId();
        if(authorId.equals(dto.getMemberId())) {
            postRepository.delete(findPost);
        } else {
            throw new AuthenticationException(AuthenticationErrorCode.UNAUTHORIZED_ACCESS,
                    "you are not allowed to edit this post");
        }
    }


    /**
     * post에 comment를 추가하는 메서드. 매개변수의 parentComment의 경우, 대댓글에서 상위 댓글의 id이며
     * 해당 comment 자체를 Comment 테이블에 추가하고, parentComment에 List로 추가한다.
     * 이후 대대댓글인지에 대한 여부를 확인하고 맞으면 오류를 반환한다(최대 대댓글 까지만 허용된다)
     *
     * @tested true
     * @param dto text, postId, parentCommentId, username
     */
    @Override
    @Transactional
    public void addCommentToPost(CreateCommentDto dto) {

        Post post = postRepository.findById(dto.getPostId()).orElseThrow(() ->
                new UserBoardException(BoardErrorCode.UNKNOWN_POST));
        Member member = memberRepository.findByUsername(dto.getUsername()).orElseThrow(() ->
                new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));

        Comment comment = Comment.builder()
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

            if(parentComment.getLayer() != 0) {
                throw new BoardManageException(BoardErrorCode.COMMENT_LAYER_OVERHEAD);
            }
            comment.setLayer(1);
            comment.setParentComment(parentComment);
            parentComment.addReply(comment);
            commentRepository.save(parentComment); // Save the parent with the new reply

        } else {
            post.addComment(comment);
            postRepository.save(post);
        }
    }



    //TODO: 매개변수를 DTO로 변경  ,  만약 reply가 있다면 삭제가 아닌 빈 comment로 만들어야함.
    @Override
    @Transactional
    public void deleteCommentFromPost(Long postId, Comment comment) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new UserBoardException(BoardErrorCode.UNKNOWN_POST));
        post.deleteComment(comment);
        postRepository.save(post);
    }


    //TODO: 조회수 말고도 좋아요, 댓글 수 카운트를 반환해야됨. 해당 로직이 완성되면 리펙토링 해야됨.
    @Override
    @Transactional(readOnly = true)
    public Page<PostThumbnailDto> getPostsByBoard(GetBoardDto dto) {
        return postRepository.findByBoardId(dto.getBoardId(), dto.getPageable())
                .map(post -> PostThumbnailDto.builder()
                        .postId(post.getId())
                        .boardId(post.getBoard().getId())
                        .authorNickname(post.getAuthor().getNickname())
                        .title(post.getTitle())
                        .text(post.getText())
                        .createdDate(post.getCreatedDate())
                        .modifiedDate(post.getModifiedDate())
                        .views(post.getViews())
                        .build());
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

    @Override
    @Transactional
    public void increaseViewCount(PostAuthorDto dto) {
        int MAX_SIZE = 300;
        long EXPIRE_HOURS = 4;

        Long userId = memberRepository.findByUsername(dto.getUsername()).orElseThrow(() ->
                new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER)).getId();
        Long postId = dto.getPostId();
        String sortedSetKey = "views:" + userId;

        String postKey = String.valueOf(postId);

        Long rank = redisRepository.getRank(sortedSetKey, postKey);

        if(rank == null) {
            postRepository.increaseView(postId);

            long currentSeconds = System.currentTimeMillis() / 1000;
            double score = currentSeconds % 999999;

            redisRepository.addToSortedSet(sortedSetKey, postKey, score);

            Long size = redisRepository.getSortedSetSize(sortedSetKey);

            if(size != null && size > MAX_SIZE) {
                redisRepository.removeRangeFromSortedSet(sortedSetKey, 0, size - MAX_SIZE - 1);
            }

            redisRepository.setExpire(sortedSetKey, EXPIRE_HOURS);
        } else {
            redisRepository.setExpire(sortedSetKey, EXPIRE_HOURS);
        }
    }

    @Override
    public void increaseLikesCount(Long postId, Long memberId) {

        Member member = findMember(memberId);
        Post post = findPost(postId);
        LikesMap likesMap = LikesMap.builder()
                .member(member)
                .post(post)
                .build();

        likesMapRepository.save(likesMap);

        String redisKey = "post:likes:" + postId;

        redisRepository.increaseValue(redisKey);
    }

    @Override
    public void decreaseLikesCount(Long postId, Long memberId) {
        LikesMapId likesMapId = LikesMapId.builder()
                .member(memberId)
                .post(postId)
                .build();

        try {
            likesMapRepository.deleteById(likesMapId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserBoardException(BoardErrorCode.NOT_EXISTING,
                    "perhaps, user may not commit likes in DB");
        }

        redisRepository.decreaseValue("post:likes:" + postId);
    }




    private Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() ->
                new BoardManageException(BoardErrorCode.UNKNOWN_BOARD));
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new BoardManageException(BoardErrorCode.UNKNOWN_POST));
    }

    private Member findMember(Long id) {
        return memberRepository.findById(id).orElseThrow(() ->
                new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));
    }

    private Member findMember(String username) {
        return memberRepository.findByUsername(username).orElseThrow(() ->
                new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));
    }

    private boolean isUserMatchingBoard(Long univId, Board board) {
        return univId.equals(board.getUniversity().getId());


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
