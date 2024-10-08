package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dao.BasePostRepository;
import com.onebucket.domain.boardManage.dao.BoardRepository;
import com.onebucket.domain.boardManage.dao.CommentRepository;
import com.onebucket.domain.boardManage.dao.LikesMapRepository;
import com.onebucket.domain.boardManage.dto.internal.comment.CreateCommentDto;
import com.onebucket.domain.boardManage.dto.internal.comment.GetCommentDto;
import com.onebucket.domain.boardManage.dto.internal.post.*;
import com.onebucket.domain.boardManage.dto.parents.PostDto;
import com.onebucket.domain.boardManage.dto.parents.ValueDto;
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
import com.onebucket.global.minio.MinioRepository;
import com.onebucket.global.minio.MinioSaveInfoDto;
import com.onebucket.global.redis.RedisRepository;
import com.onebucket.global.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.optional.qual.Present;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.service
 * <br>file name      : AbstractPostService
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


@RequiredArgsConstructor
public abstract class AbstractPostService<T extends Post, R extends BasePostRepository<T>> implements BasePostService{

    protected final R repository;
    protected final BoardRepository boardRepository;
    protected final MemberRepository memberRepository;
    protected final SecurityUtils securityUtils;
    protected final CommentRepository commentRepository;
    protected final RedisRepository redisRepository;
    protected final LikesMapRepository likesMapRepository;
    protected final MinioRepository minioRepository;

    @NestedConfigurationProperty
    @Value("${board.views.maxSize}")
    private int MAX_SIZE;

    @NestedConfigurationProperty
    @Value("${board.views.expireHour}")
    private long EXPIRE_HOUR;



    @Override
    public <D extends PostDto.Create> Long createPost(D dto) {

        T post = convertCreatePostDtoToPost(dto);
        T savedPost = repository.save(post);

        return savedPost.getId();
    }
    @Override
    public void deletePost(ValueDto.FindPost dto) {
        T findPost = findPost(dto.getPostId());
        Member author = findPost.getAuthor();
        if (author == null) {
            throw new UserBoardException(BoardErrorCode.I_AM_AN_APPLE_PIE, "maybe, author is null");
        }
        Long authorId = author.getId();

        if(authorId.equals(dto.getUserId())) {
            repository.delete(findPost);
        } else {
            throw new AuthenticationException(AuthenticationErrorCode.UNAUTHORIZED_ACCESS,
                    "you are not allowed to edit this post");
        }

    }
    @Override
    @Transactional
    @CacheEvict(value = "commentCountCache", key = "#dto.postId")
    public void addCommentToPost(CreateCommentDto dto) {
        T post = findPost(dto.getPostId());
        Member member = findMember(dto.getUsername());

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
                throw new UserBoardException(BoardErrorCode.COMMENT_LAYER_OVERHEAD);
            }
            comment.setLayer(1);
            comment.setParentComment(parentComment);
            parentComment.addReply(comment);
            commentRepository.save(parentComment); // Save the parent with the new reply

        } else {
            post.addComment(comment);
            repository.save(post);
        }
    }
    @Override
    @Transactional
    @CacheEvict(value = "commentCountCache", key = "#dto.postId")
    public void deleteCommentFromPost(ValueDto.FindComment dto) {
        T post = findPost(dto.getPostId());
        List<Comment> comments = post.getComments();
        Comment savedComment = comments.stream().filter((comment) -> comment.getId().equals(dto.getCommentId()))
                        .findFirst().orElseThrow(() -> new UserBoardException(BoardErrorCode.UNKNOWN_COMMENT));

        post.deleteComment(savedComment);
        repository.save(post);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<PostDto.Thumbnail> getPostsByBoard(ValueDto.PageablePost dto) {
        return repository.findByBoardId(dto.getBoardId(), dto.getPageable())
                .map(this::convertPostToThumbnailDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostDto.Thumbnail> getSearchResult(ValueDto.SearchPageablePost dto) {
        String keyword = dto.getKeyword();
        //1 is for title, 2 is for text, 3 is for title + text.
        Integer option = dto.getOption();
        Page<T> posts;
        if(option == 1) {
            posts = repository.searchPostsByTitle(keyword, dto.getBoardId(),dto.getPageable());
        } else if(option == 2) {
            posts = repository.searchPostsByText(keyword, dto.getBoardId(), dto.getPageable());
        } else if(option == 3) {
            posts = repository.searchPosts(keyword, dto.getBoardId(), dto.getPageable());
        } else {
            throw new UserBoardException(BoardErrorCode.UNKNOWN_SEARCH_OPTION);
        }

        return posts.map(this::convertPostToThumbnailDto);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostDto.Thumbnail> getPostByAuthorId(ValueDto.AuthorPageablePost dto) {
        return repository.findByAuthorId(dto.getUserId(), dto.getPageable())
                .map(this::convertPostToThumbnailDto);
    }



    @Override
    @Transactional(readOnly = true)
    public PostDto.Info getPost(ValueDto.GetPost dto) {
        T post = findPost(dto.getPostId());

        List<GetCommentDto> comments = post.getComments().stream()
                .filter(comment -> comment.getParentComment() == null)  // 부모 댓글이 없는(즉, 직접적인 댓글)만 필터링
                .map(this::convertToGetCommentDto)
                .toList();

        return convertPostToPostInfoDto(post, comments);

    }
    @Override
    @Transactional
    public void increaseViewCount(ValueDto.FindPost dto) {

        Long userId = dto.getUserId();
        Long postId = dto.getPostId();
        String sortedSetKey = "views:" + userId;

        String postKey = String.valueOf(postId);

        Long rank = redisRepository.getRank(sortedSetKey, postKey);

        if(rank == null) {
            repository.increaseView(postId);

            long currentSeconds = System.currentTimeMillis() / 1000;
            double score = currentSeconds % 999999;

            redisRepository.addToSortedSet(sortedSetKey, postKey, score);

            Long size = redisRepository.getSortedSetSize(sortedSetKey);

            if(size != null && size > MAX_SIZE) {
                redisRepository.removeRangeFromSortedSet(sortedSetKey, 0, size - MAX_SIZE - 1);
            }

            redisRepository.setExpire(sortedSetKey, EXPIRE_HOUR);
        } else {
            redisRepository.setExpire(sortedSetKey, EXPIRE_HOUR);
        }
    }

    @Override
    public void increaseLikesCount(ValueDto.FindPost dto) {
        Member member = findMember(dto.getUserId());
        Post post = findPost(dto.getPostId());

        LikesMapId id = LikesMapId.builder()
                .post(post.getId())
                .member(member.getId())
                .build();
        if(likesMapRepository.existsById(id)) {
            return;
        }

        LikesMap likesMap = LikesMap.builder()
                .member(member)
                .post(post)
                .build();
        likesMapRepository.save(likesMap);

        String redisKey = "post:likes:" + dto.getPostId();

        redisRepository.increaseValue(redisKey);
    }

    @Override
    public void decreaseLikesCount(ValueDto.FindPost dto) {
        LikesMapId likesMapId = LikesMapId.builder()
                .member(dto.getUserId())
                .post(dto.getPostId())
                .build();

        try {
            likesMapRepository.deleteById(likesMapId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserBoardException(BoardErrorCode.NOT_EXISTING,
                    "perhaps, user may not commit likes in DB");
        }

        redisRepository.decreaseValue("post:likes:" + dto.getPostId());

    }

    @Override
    @Cacheable(value = "commentCountCache", key = "#postId")
    public Long getCommentCount(Long postId) {
        return commentRepository.countAllByPostId(postId);
    }
    @Override
    public Long getLikesInRedis(Long postId) {
        String redisKey = "post:likes:" + postId;

        String result = redisRepository.get(redisKey);
        if(result == null) {
            return 0L;
        }
        return Long.parseLong(result);
    }

    @Override
    public boolean isUserLikesPost(ValueDto.FindPost dto) {
        LikesMapId id = LikesMapId.builder()
                .post(dto.getPostId())
                .member(dto.getUserId())
                .build();
        return likesMapRepository.existsById(id);

    }

    @Override
    @Transactional
    public void saveImage(MultipartFile multipartFile, SaveImageDto dto) {

        String url = "/post/" + dto.getPostId() + "/image/" + dto.getImageName();
        MinioSaveInfoDto minioSaveInfoDto = MinioSaveInfoDto.builder()
                .bucketName("one-bucket")
                .fileName(url)
                .fileExtension(dto.getFileExtension())
                .build();

        try {
            minioRepository.uploadFile(multipartFile,minioSaveInfoDto);
            T post = repository.findById(dto.getPostId()).orElseThrow();
            post.addImage(url + "." + dto.getFileExtension());
            repository.save(post);
        } catch (Exception e) {
            throw new UserBoardException(BoardErrorCode.I_AM_AN_APPLE_PIE, "error occur while save image");
        }

    }




    protected Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() ->
                new BoardManageException(BoardErrorCode.UNKNOWN_BOARD));
    }
    protected Member findMember(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER,
                        "can't find member while create post"));
    }

    protected Member findMember(Long id) {
        return memberRepository.findById(id).orElseThrow(() ->
                new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));
    }
    protected T findPost(Long id) {
        return repository.findById(id).orElseThrow(() ->
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

    protected abstract <D extends PostDto.Create> T convertCreatePostDtoToPost(D dto);
    protected abstract PostDto.Thumbnail convertPostToThumbnailDto(T post);
    protected abstract PostDto.Info convertPostToPostInfoDto(T post, List<GetCommentDto> comments);


}
