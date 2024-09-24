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
                //일반 post value
                .board(board)
                .author(member)
                .title(dto.getTitle())
                .text(dto.getText())
                //market value
                .item(marketDto.getItem())
                .price(marketDto.getPrice())
                .count(marketDto.getCount())
                .location(marketDto.getLocation())
                .wanted(marketDto.getWanted())
                .dueDate(marketDto.getDueDate())
                .isFin(false)
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

                .item(post.getItem())
                .joins(post.getJoins())
                .wanted(post.getWanted())
                .dueDate(post.getDueDate())
                .price(post.getPrice())
                .count(post.getCount())
                .isFin(post.isFin())
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
                .boardId(post.getBoardId())
                .authorNickname(nickname)
                .title(post.getTitle())
                .text(post.getText())
                .comments(comments)
                .likes(post.getLikes())
                .views(post.getViews())
                .createdDate(post.getCreatedDate())
                .modifiedDate(post.getModifiedDate())

                .item(post.getItem())
                .wanted(post.getWanted())
                .joins(post.getJoins())
                .isFin(post.isFin())
                .location(post.getLocation())
                .price(post.getPrice())
                .count(post.getCount())
                .dueDate(post.getDueDate())
                .build();
    }

}
