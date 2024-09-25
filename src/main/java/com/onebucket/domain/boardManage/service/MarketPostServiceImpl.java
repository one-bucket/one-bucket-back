package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dao.*;
import com.onebucket.domain.boardManage.dto.internal.post.*;
import com.onebucket.domain.boardManage.dto.internal.comment.GetCommentDto;
import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.boardManage.entity.post.MarketPost;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;

import com.onebucket.domain.tradeManage.entity.PendingTrade;
import com.onebucket.domain.tradeManage.dao.PendingTradeRepository;
import com.onebucket.global.minio.MinioRepository;
import com.onebucket.global.redis.RedisRepository;
import com.onebucket.global.utils.SecurityUtils;
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


    private final PendingTradeRepository pendingTradeRepository;

    public MarketPostServiceImpl(MarketPostRepository repository,
                                 BoardRepository boardRepository,
                                 MemberRepository memberRepository,
                                 SecurityUtils securityUtils,
                                 CommentRepository commentRepository,
                                 RedisRepository redisRepository,
                                 LikesMapRepository likesMapRepository,
                                 MinioRepository minioRepository,
                                 PendingTradeRepository pendingTradeRepository) {
        super(repository, boardRepository, memberRepository, securityUtils,
                commentRepository, redisRepository, likesMapRepository, minioRepository);
        this.pendingTradeRepository = pendingTradeRepository;
    }

    @Override
    protected <D extends CreatePostDto> MarketPost convertCreatePostDtoToPost(D dto) {
        Member member = findMember(dto.getUsername());
        Board board = findBoard(dto.getBoardId());

        CreateMarketPostDto marketDto = (CreateMarketPostDto) dto;

        PendingTrade tradeInfo = PendingTrade.builder()
                .item(marketDto.getItem())
                .price(marketDto.getPrice())
                .count(marketDto.getCount())
                .location(marketDto.getLocation())
                .dueDate(marketDto.getDueDate())
                .wanted(marketDto.getWanted())
                .isFin(false)
                .build();
        tradeInfo.addMember(member);

        PendingTrade savedPendingTrade = pendingTradeRepository.save(tradeInfo);

        return MarketPost.builder()
                //일반 post value
                .board(board)
                .author(member)
                .title(dto.getTitle())
                .text(dto.getText())
                .pendingTrade(savedPendingTrade)
                .build();

    }

    @Override
    protected MarketPostThumbnailDto convertPostToThumbnailDto(MarketPost post) {

        String nickname = "(unknown)";
        Member member = post.getAuthor();
        if(member != null) {
            nickname = member.getNickname();
        }

        //실제 썸네일에 들어갈 내용에 대해 앞 n 글자만 반환
        String preText = post.getText().substring(50);

        boolean isImageExist = true;
        String imageUrl = "fail:null image";
        List<String> images = post.getImageUrls();
        if(images.isEmpty()) {
            isImageExist = false;
        } else {
            imageUrl = images.get(1);
        }

        //거래에 대한 실질적인 정보를 담은 테이블 로드
        PendingTrade pendingTrade = post.getPendingTrade();

        return MarketPostThumbnailDto.builder()
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
                .isImageExist(isImageExist)
                .thumbnailImage(imageUrl)

                .item(pendingTrade.getItem())
                .joins(pendingTrade.getJoins())
                .wanted(pendingTrade.getWanted())
                .dueDate(pendingTrade.getDueDate())
                .price(pendingTrade.getPrice())
                .count(pendingTrade.getCount())
                .isFin(pendingTrade.isFin())
                .build();
    }

    @Override
    protected MarketPostInfoDto convertPostToPostInfoDto(MarketPost post, List<GetCommentDto> comments) {
        String nickname = "(unknown)";
        Member member = post.getAuthor();
        if (member != null) {
            nickname = member.getNickname();
        }

        PendingTrade pendingTrade = post.getPendingTrade();

        return MarketPostInfoDto.builder()
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

                .item(pendingTrade.getItem())
                .wanted(pendingTrade.getWanted())
                .joins(pendingTrade.getJoins())
                .isFin(pendingTrade.isFin())
                .location(pendingTrade.getLocation())
                .price(pendingTrade.getPrice())
                .count(pendingTrade.getCount())
                .dueDate(pendingTrade.getDueDate())

                .pendingId(pendingTrade.getId())
                .build();
    }

}
