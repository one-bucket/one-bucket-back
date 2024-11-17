package com.onebucket.domain.boardManage.service.postService;

import com.onebucket.domain.boardManage.dao.postRepository.BasePostRepository;
import com.onebucket.domain.boardManage.dao.BoardRepository;
import com.onebucket.domain.boardManage.dao.CommentRepository;
import com.onebucket.domain.boardManage.dao.LikesMapRepository;
import com.onebucket.domain.boardManage.dto.internal.comment.CreateCommentDto;
import com.onebucket.domain.boardManage.dto.internal.comment.GetCommentDto;
import com.onebucket.domain.boardManage.dto.internal.post.*;
import com.onebucket.domain.boardManage.dto.postDto.PostDto;
import com.onebucket.domain.boardManage.dto.postDto.PostKeyDto;
import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.boardManage.entity.Comment;
import com.onebucket.domain.boardManage.entity.LikesMap;
import com.onebucket.domain.boardManage.entity.LikesMapId;
import com.onebucket.domain.boardManage.entity.post.GroupTradePost;
import com.onebucket.domain.boardManage.entity.post.Post;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.global.exceptionManage.customException.boardManageException.BoardManageException;
import com.onebucket.global.exceptionManage.customException.boardManageException.UserBoardException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.BoardErrorCode;
import com.onebucket.global.minio.MinioRepository;
import com.onebucket.global.minio.MinioInfoDto;
import com.onebucket.global.redis.RedisRepository;
import com.onebucket.global.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.service
 * <br>file name      : AbstractPostService
 * <br>date           : 2024-09-21
 * <pre>
 * <span style="color: white;">[description]</span>
 * PostService에 관련된 추상 클래스이다. 이에 대한 구현 클래스는 {@link PostService} 와 {@link GroupTradePostService} 이다.
 * 각 메서드는 {@link BasePostService} 에서 정의되어 있으며 이는 유사하나 다른 두 엔티티
 * {@link Post} 와 {@link GroupTradePost MarketPost} 를 사용한다.
 * 두 엔티티는 각기 다른 DAO를 가지고 있고 따라서 엔티티와 DAO에 관한 제네릭을 사용하여 정의하였다.
 *
 * 메서드가 겹치는 경우, 해당 클래스에서 정의하였고, 칼럼의 차이로 안해 변경이 생기는 경우, 이를 추상 메서드로 선언하거나
 * 동일한 부분을 추출하여 정의한 뒤, protected abstract  메서드를 선언하여 이를 구현하는 구현 클래스에서
 * 나머지를 채우도록 하였다.
 * </pre>
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

    @NestedConfigurationProperty
    @Value("${minio.bucketName}")
    private String BUCKET_NAME;


    //CREATE
    @Override
    @Transactional
    public <D extends PostDto.Create> Long createPost(D dto) {

        T post = convertCreatePostDtoToPost(dto);
        T savedPost = repository.save(post);

        return savedPost.getId();
    }

    //READ
    @Override
    @Transactional(readOnly = true)
    public Page<PostDto.InternalThumbnail> getPostsByBoard(PostKeyDto.BoardPage dto) {
        return repository.findByBoardId(dto.getBoardId(), dto.getPageable())
                .map(this::convertPostToThumbnail);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<PostDto.InternalThumbnail> getSearchResult(PostKeyDto.SearchPage dto) {
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

        return posts.map(this::convertPostToThumbnailDtoInternal);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostDto.InternalThumbnail> getPostByAuthorId(PostKeyDto.AuthorPage dto) {
        return repository.findByAuthorId(dto.getUserId(), dto.getPageable())
                .map(this::convertPostToThumbnailDtoInternal);
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto.Info getPost(PostKeyDto.PostKey dto) {
        T post = findPost(dto.getPostId());

        List<GetCommentDto> comments = post.getComments().stream()
                .filter(comment -> comment.getParentComment() == null)
                .map(this::convertToGetCommentDto)
                .toList();

        return convertPostToInfo(post, comments);

    }

    //UPDATE
    @Override
    @Transactional
    public <D extends PostDto.Update> Long updatePost(D dto) {
        T post = findPost(dto.getPostId());
        post.setTitle(dto.getTitle());
        post.setText(dto.getText());
        post.setModified(true);
        post.setModifiedDate(LocalDateTime.now());

        return repository.save(post).getId();
    }

    @Override
    @Transactional
    public void increaseViewCount(PostKeyDto.UserPost dto) {

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
    public void increaseLikesCount(PostKeyDto.UserPost dto) {
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
    public void decreaseLikesCount(PostKeyDto.UserPost dto) {
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

    //DELETE
    @Override
    @Transactional
    public void deletePost(PostKeyDto.PostKey dto) {
        T findPost = findPost(dto.getPostId());

        repository.delete(findPost);

        initPostImage(dto.getPostId());
    }

    @Override
    @Transactional
    public void deleteImageOnPost(Long postId) {
        initPostImage(postId);

        T post = findPost(postId);
        post.initImage();
        repository.save(post);
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
    public void deleteCommentFromPost(PostKeyDto.CommentKey dto) {
        Comment comment = commentRepository.findById(dto.getCommentId()).orElseThrow(() ->
                new UserBoardException(BoardErrorCode.UNKNOWN_COMMENT));
        Long commentPostId = comment.getPostId();
        if(!dto.getPostId().equals(commentPostId)) {
            throw new UserBoardException(BoardErrorCode.UNKNOWN_POST);
        }
        commentRepository.delete(comment);
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
    public boolean isUserLikesPost(PostKeyDto.UserPost dto) {
        LikesMapId id = LikesMapId.builder()
                .post(dto.getPostId())
                .member(dto.getUserId())
                .build();
        return likesMapRepository.existsById(id);

    }

    @Override
    @Transactional
    public void saveImage(MultipartFile multipartFile, SaveImageDto dto) {

        String url = "post/" + dto.getPostId() + "/image/" + dto.getImageName();
        MinioInfoDto minioInfoDto = MinioInfoDto.builder()
                .bucketName(BUCKET_NAME)
                .fileName(url)
                .fileExtension(dto.getFileExtension())
                .build();

        try {
            minioRepository.uploadFile(multipartFile, minioInfoDto);
            T post = repository.findById(dto.getPostId()).orElseThrow();
            post.addImage(url + "." + dto.getFileExtension());
            repository.save(post);
        } catch (Exception e) {
            throw new UserBoardException(BoardErrorCode.I_AM_AN_APPLE_PIE, "error occur while save image");
        }

    }

    private void initPostImage(Long postId) {
        MinioInfoDto deleteDto = MinioInfoDto.builder()
                .bucketName(BUCKET_NAME)
                .fileName("post/"+ postId + "/image/")
                .build();
        minioRepository.deleteDirectory(deleteDto);


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

    protected PostDto.InternalThumbnail convertPostToThumbnail(T post) {
        PostDto.InternalThumbnail thumbnail = convertPostToThumbnailDtoInternal(post);
        String text = thumbnail.getText();
        if(text.length() > 50) {
            text = text.substring(0, 50);
        }
        thumbnail.setText(text);
        return thumbnail;
    }

    protected PostDto.Info convertPostToInfo(T post, List<GetCommentDto> comments) {
        PostDto.Info info = (PostDto.Info) convertPostToThumbnailDtoInternal(post);
        info.setComments(comments);

        return info;
    }

    protected PostDto.InternalThumbnail convertPostToThumbnailDtoInternal(T post) {
        Long authorId = -1L;
        String authorNickname = "(unknown)";
        if(post.getAuthor() != null) {
            authorId = post.getAuthorId();
            authorNickname = post.getAuthor().getNickname();
        }
        String text = post.getText();
        if(text.length() > 50) {
            text = post.getText().substring(0, 50);
        }

        PostDto.InternalThumbnail thumbnail = PostDto.InternalThumbnail.builder()
                .postId(post.getId())
                .authorId(authorId)
                .authorNickname(authorNickname)
                .title(post.getTitle())
                .text(text)
                .createdDate(post.getCreatedDate())
                .modifiedDate(post.getModifiedDate())
                .imageUrls(post.getImageUrls())
                .views(post.getViews())
                .likes(post.getLikes())
                .build();

        return setThumbnailForOtherInfo(thumbnail, post);
    }
    protected abstract PostDto.InternalThumbnail setThumbnailForOtherInfo(PostDto.InternalThumbnail dto, T post);
    protected abstract <D extends PostDto.Create> T convertCreatePostDtoToPost(D dto);
}
