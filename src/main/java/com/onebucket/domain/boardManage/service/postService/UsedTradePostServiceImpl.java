package com.onebucket.domain.boardManage.service.postService;

import com.onebucket.domain.boardManage.dao.BoardRepository;
import com.onebucket.domain.boardManage.dao.CommentRepository;
import com.onebucket.domain.boardManage.dao.LikesMapRepository;
import com.onebucket.domain.boardManage.dao.postRepository.UsedTradePostRepository;
import com.onebucket.domain.boardManage.dto.internal.comment.GetCommentDto;
import com.onebucket.domain.boardManage.dto.postDto.PostDto;
import com.onebucket.domain.boardManage.dto.postDto.UsedTradePostDto;
import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.boardManage.entity.post.UsedTradePost;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.tradeManage.dao.pendingTrade.UsedTradeRepository;
import com.onebucket.domain.tradeManage.entity.UsedTrade;
import com.onebucket.global.minio.MinioRepository;
import com.onebucket.global.redis.RedisRepository;
import com.onebucket.global.utils.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.service.postService
 * <br>file name      : UsedTradePostServiceImpl
 * <br>date           : 2024-11-17
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
@Service
public class UsedTradePostServiceImpl extends AbstractPostService<UsedTradePost, UsedTradePostRepository>
implements UsedTradePostService {
    private final UsedTradeRepository usedTradeRepository;

    public UsedTradePostServiceImpl(UsedTradePostRepository repository,
                                    BoardRepository boardRepository,
                                    MemberRepository memberRepository,
                                    SecurityUtils securityUtils,
                                    CommentRepository commentRepository,
                                    RedisRepository redisRepository,
                                    LikesMapRepository likesMapRepository,
                                    MinioRepository minioRepository,
                                    UsedTradeRepository usedTradeRepository) {
        super(repository, boardRepository, memberRepository, securityUtils,
                commentRepository, redisRepository, likesMapRepository, minioRepository);
        this.usedTradeRepository = usedTradeRepository;
    }


    @Override
    @SuppressWarnings("unchecked")
    protected <D extends PostDto.Info> D convertPostToInfo(UsedTradePost post, List<GetCommentDto> comments) {
        UsedTradePostDto.InternalThumbnail internalThumbnail = convertPostToThumbnailDtoInternal(post);

        UsedTradePostDto.Info response = UsedTradePostDto.Info.of(internalThumbnail);
        response.setComments(comments);
        return (D) response;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected UsedTradePostDto.InternalThumbnail convertPostToThumbnailDtoInternal(UsedTradePost post) {
        Long authorId = -1L;
        String authorNickname = "(unknown)";
        if (post.getAuthor() != null) {
            authorId = post.getAuthorId();
            authorNickname = post.getAuthor().getNickname();
        }
        String text = post.getText();
        return UsedTradePostDto.InternalThumbnail.builder()
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
                .trade(post.getUsedTradeId())
                .liftedAt(post.getLiftedAt())
                .build();
    }

    @Override
    protected <D extends PostDto.Create> UsedTradePost convertCreatePostDtoToPost(D dto) {
        UsedTradePostDto.Create createDto = (UsedTradePostDto.Create) dto;

        Member member = findMember(createDto.getUserId());
        Board board = findBoard(createDto.getBoardId());

        UsedTrade usedTrade = usedTradeRepository.getReferenceById(createDto.getTradeId());

        return UsedTradePost.builder()
                .board(board)
                .author(member)
                .title(createDto.getTitle())
                .text(createDto.getText())
                .usedTrade(usedTrade)
                .build();
    }

    @Override
    public Page<UsedTradePostDto.InternalThumbnail> getPostByTradeIdList(UsedTradePostDto.TradeIdsPageDto dto) {
        return repository.findByTradeIds(dto.getTradeIds(), dto.getPageable())
                .map((post) -> (UsedTradePostDto.InternalThumbnail) convertPostToThumbnail(post));
    }
}
