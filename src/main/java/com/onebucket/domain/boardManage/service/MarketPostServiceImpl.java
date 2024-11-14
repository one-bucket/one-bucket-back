package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dao.*;
import com.onebucket.domain.boardManage.dto.internal.comment.GetCommentDto;
import com.onebucket.domain.boardManage.dto.parents.MarketPostDto;
import com.onebucket.domain.boardManage.dto.parents.PostDto;
import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.boardManage.entity.post.MarketPost;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;

import com.onebucket.domain.tradeManage.dao.pendingTrade.GroupTradeRepository;
import com.onebucket.domain.tradeManage.entity.GroupTrade;
import com.onebucket.global.minio.MinioRepository;
import com.onebucket.global.redis.RedisRepository;
import com.onebucket.global.utils.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.service
 * <br>file name      : MarketPostServiceImpl
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
@Service
public class MarketPostServiceImpl extends AbstractPostService<MarketPost, MarketPostRepository> implements MarketPostService {


    private final GroupTradeRepository groupTradeRepository;

    public MarketPostServiceImpl(MarketPostRepository repository,
                                 BoardRepository boardRepository,
                                 MemberRepository memberRepository,
                                 SecurityUtils securityUtils,
                                 CommentRepository commentRepository,
                                 RedisRepository redisRepository,
                                 LikesMapRepository likesMapRepository,
                                 MinioRepository minioRepository,
                                 GroupTradeRepository groupTradeRepository, MarketPostRepository marketPostRepository) {
        super(repository, boardRepository, memberRepository, securityUtils,
                commentRepository, redisRepository, likesMapRepository, minioRepository);
        this.groupTradeRepository = groupTradeRepository;
    }

    @Override
    public Page<MarketPostDto.Thumbnail> getPostByTradeIdList(List<Long> tradeIds, Pageable pageable) {
        return repository.findByPendingTradeIds(tradeIds, pageable)
                .map(this::convertPostToThumbnailDto);
    }

    @Override
    protected <D extends PostDto.Create> MarketPost convertCreatePostDtoToPost(D dto) {
        Member member = findMember(dto.getUserId());
        Board board = findBoard(dto.getBoardId());

        MarketPostDto.Create marketDto = (MarketPostDto.Create) dto;

        GroupTrade groupTrade = groupTradeRepository.getReferenceById(marketDto.getTradeId());

        return MarketPost.builder()
                .board(board)
                .author(member)
                .title(dto.getTitle())
                .text(dto.getText())
                .groupTrade(groupTrade)
                .build();
    }

    @Override
    protected MarketPostDto.Thumbnail convertPostToThumbnailDto(MarketPost post) {

        String nickname = "(unknown)";
        Member member = post.getAuthor();
        if(member != null) {
            nickname = member.getNickname();
        }

        String preText = "init text";
        String text = post.getText();
        if(text.length() > 50) {
            preText = post.getText().substring(0, 50);
        } else {
            preText = text;
        }


        return MarketPostDto.Thumbnail.builder()
                //value of post
                .postId(post.getId())
                .boardId(post.getBoardId())
                .authorNickname(nickname)
                .title(post.getTitle())
                .text(preText)
                .likes(post.getLikes())
                .views(post.getViews())
                .createdDate(post.getCreatedDate())
                .modifiedDate(post.getModifiedDate())
                .imageUrls(post.getImageUrls())
                .tradeId(post.getGroupTrade().getId())
                .build();
    }

    @Override
    protected MarketPostDto.Info convertPostToPostInfoDto(MarketPost post, List<GetCommentDto> comments) {
        String nickname = "(unknown)";
        Member member = post.getAuthor();
        if (member != null) {
            nickname = member.getNickname();
        }

        return MarketPostDto.Info.builder()
                .postId(post.getId())
                .boardId(post.getBoardId())
                .authorNickname(nickname)
                .title(post.getTitle())
                .text(post.getText())
                .comments(comments)
                .likes(post.getLikes())
                .views(post.getViews())
                .createdDate(post.getCreatedDate())
                .modifiedDate(post.getModifiedDate())

                .tradeId(post.getGroupTrade().getId())
                .build();
    }

}
