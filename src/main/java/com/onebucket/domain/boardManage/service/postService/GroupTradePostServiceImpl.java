package com.onebucket.domain.boardManage.service.postService;

import com.onebucket.domain.boardManage.dao.BoardRepository;
import com.onebucket.domain.boardManage.dao.CommentRepository;
import com.onebucket.domain.boardManage.dao.postRepository.GroupTradePostRepository;
import com.onebucket.domain.boardManage.dao.LikesMapRepository;
import com.onebucket.domain.boardManage.dto.internal.comment.GetCommentDto;
import com.onebucket.domain.boardManage.dto.postDto.GroupTradePostDto;
import com.onebucket.domain.boardManage.dto.postDto.PostDto;
import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.boardManage.entity.post.GroupTradePost;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.tradeManage.dao.pendingTrade.GroupTradeRepository;
import com.onebucket.domain.tradeManage.entity.GroupTrade;
import com.onebucket.global.minio.MinioRepository;
import com.onebucket.global.redis.RedisRepository;
import com.onebucket.global.utils.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


/**
 * <br>package name   : com.onebucket.domain.boardManage.service
 * <br>file name      : GroupTradePostServiceImpl
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
public class GroupTradePostServiceImpl extends AbstractPostService<GroupTradePost, GroupTradePostRepository>
implements GroupTradePostService {

    private final GroupTradeRepository groupTradeRepository;
    public GroupTradePostServiceImpl(GroupTradePostRepository repository,
                                     BoardRepository boardRepository,
                                     MemberRepository memberRepository,
                                     SecurityUtils securityUtils,
                                     CommentRepository commentRepository,
                                     RedisRepository redisRepository,
                                     LikesMapRepository likesMapRepository,
                                     MinioRepository minioRepository,
                                     GroupTradeRepository groupTradeRepository) {
        super(repository, boardRepository, memberRepository, securityUtils,
                commentRepository, redisRepository, likesMapRepository, minioRepository);
        this.groupTradeRepository = groupTradeRepository;
    }


    @Override
    protected <D extends PostDto.Create> GroupTradePost convertCreatePostDtoToPost(D dto) {

        GroupTradePostDto.Create createDto = (GroupTradePostDto.Create) dto;

        Member member = findMember(createDto.getUserId());
        Board board = findBoard(createDto.getBoardId());

        GroupTrade groupTrade = groupTradeRepository.getReferenceById(createDto.getTradeId());

        return GroupTradePost.builder()
                .board(board)
                .author(member)
                .title(createDto.getTitle())
                .text(createDto.getText())
                .groupTrade(groupTrade)
                .liftedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public Page<GroupTradePostDto.InternalThumbnail> getPostByTradeIdList(GroupTradePostDto.TradeIdsPageDto dto) {
        return repository.findByTradeIds(dto.getTradeIds(), dto.getPageable())
                .map((post) -> (GroupTradePostDto.InternalThumbnail) convertPostToThumbnail(post));
    }


    @Override
    @SuppressWarnings("unchecked")
    protected <D extends PostDto.Info> D convertPostToInfo(GroupTradePost post, List<GetCommentDto> comments) {
        GroupTradePostDto.InternalThumbnail internalThumbnail = convertPostToThumbnailDtoInternal(post);

        GroupTradePostDto.Info response = GroupTradePostDto.Info.of(internalThumbnail);
        response.setComments(comments);
        return (D) response;

    }


    @Override
    @SuppressWarnings("unchecked")
    protected GroupTradePostDto.InternalThumbnail convertPostToThumbnailDtoInternal(GroupTradePost post) {
        Long authorId = -1L;
        String authorNickname = "(unknown)";
        if (post.getAuthor() != null) {
            authorId = post.getAuthorId();
            authorNickname = post.getAuthor().getNickname();
        }
        String text = post.getText();

        return GroupTradePostDto.InternalThumbnail.builder()
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
                .trade(post.getGroupTradeId())
                .liftedAt(post.getLiftedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTradeId(Long postId) {
        GroupTradePost groupTradePost = findPost(postId);
        return groupTradePost.getGroupTradeId();
    }
}
