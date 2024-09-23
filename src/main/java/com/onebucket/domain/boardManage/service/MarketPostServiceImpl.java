package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dao.*;
import com.onebucket.domain.boardManage.dto.internal.post.*;
import com.onebucket.domain.boardManage.dto.internal.comment.GetCommentDto;
import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.boardManage.entity.post.MarketPost;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;

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

    public MarketPostServiceImpl(MarketPostRepository repository,
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
    protected <D extends CreatePostDto> MarketPost convertCreatePostDtoToPost(D dto) {
        Member member = findMember(dto.getUsername());
        Board board = findBoard(dto.getBoardId());

        CreateMarketPostDto marketDto = (CreateMarketPostDto) dto;
        return MarketPost.builder()
                .isFin(false)
                .wanted(marketDto.getWanted())
                .item(marketDto.getItem())
                .location(marketDto.getLocation())
                .author(member)
                .title(marketDto.getTitle())
                .text(marketDto.getText())
                .board(board)
                .build();

    }

    @Override
    protected MarketPostThumbnailDto convertPostToThumbnailDto(MarketPost post) {

        String nickname = "(unknown)";
        Member member = post.getAuthor();
        if(member != null) {
            nickname = member.getNickname();
        }


        return MarketPostThumbnailDto.builder()
                .joins(post.getJoins())
                .item(post.getItem())
                .isFin(post.isFin())
                .likes(post.getLikes())
                .wanted(post.getWanted())
                .authorNickname(nickname)
                .createdDate(post.getCreatedDate())
                .modifiedDate(post.getModifiedDate())
                .boardId(post.getBoardId())
                .title(post.getTitle())
                .text(post.getText())
                .postId(post.getId())
                .views(post.getViews())
                .build();
    }

    @Override
    protected MarketPostInfoDto convertPostToPostInfoDto(MarketPost post, List<GetCommentDto> comments) {
        String nickname = "(unknown)";
        Member member = post.getAuthor();
        if (member != null) {
            nickname = member.getNickname();
        }

        return MarketPostInfoDto.builder()
                .postId(post.getId())
                .views(post.getViews())
                .likes(post.getLikes())
                .title(post.getTitle())
                .text(post.getText())
                .boardId(post.getBoardId())
                .authorNickname(nickname)
                .createdDate(post.getCreatedDate())
                .modifiedDate(post.getModifiedDate())
                .comments(comments)
                .item(post.getItem())
                .wanted(post.getWanted())
                .joins(post.getJoins())
                .isFin(post.isFin())
                .location(post.getLocation())
                .build();
    }

}
