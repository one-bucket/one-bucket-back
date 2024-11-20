package com.onebucket.domain.boardManage.service.postService;

import com.onebucket.domain.boardManage.dao.BoardRepository;
import com.onebucket.domain.boardManage.dao.CommentRepository;
import com.onebucket.domain.boardManage.dao.LikesMapRepository;
import com.onebucket.domain.boardManage.dao.postRepository.PostRepository;

import com.onebucket.domain.boardManage.dto.internal.comment.GetCommentDto;
import com.onebucket.domain.boardManage.dto.postDto.PostDto;
import com.onebucket.domain.boardManage.entity.Board;

import com.onebucket.domain.boardManage.entity.post.Post;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;

import com.onebucket.global.minio.MinioRepository;
import com.onebucket.global.redis.RedisRepository;
import com.onebucket.global.utils.SecurityUtils;

import org.springframework.stereotype.Service;

import java.util.List;


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


@Service
public class PostServiceImpl extends AbstractPostService<Post, PostRepository> implements PostService {


    public PostServiceImpl(PostRepository repository,
                           BoardRepository boardRepository,
                           MemberRepository memberRepository,
                           SecurityUtils securityUtils,
                           CommentRepository commentRepository,
                           RedisRepository redisRepository,
                           LikesMapRepository likesMapRepository,
                           MinioRepository minioRepository) {
        super(repository, boardRepository, memberRepository, securityUtils,
                commentRepository, redisRepository, likesMapRepository, minioRepository);
    }

    @Override
    protected Post convertCreatePostDtoToPost(PostDto.Create dto) {
        Member member = findMember(dto.getUserId());
        Board board = findBoard(dto.getBoardId());

        return Post.builder()
                .title(dto.getTitle())
                .text(dto.getText())
                .author(member)
                .board(board)
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <D extends PostDto.Info> D convertPostToInfo(Post post, List<GetCommentDto> comments) {
        PostDto.InternalThumbnail internalThumbnail = convertPostToThumbnailDtoInternal(post);

        PostDto.Info response = PostDto.Info.of(internalThumbnail);
        response.setComments(comments);
        return (D) response;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected PostDto.InternalThumbnail convertPostToThumbnailDtoInternal(Post post) {
        Long authorId = -1L;
        String authorNickname = "(unknown)";
        if (post.getAuthor() != null) {
            authorId = post.getAuthorId();
            authorNickname = post.getAuthor().getNickname();
        }
        String text = post.getText();

        return PostDto.InternalThumbnail.builder()
                .boardId(post.getBoardId())
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
    }
}
