package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dao.BoardRepository;
import com.onebucket.domain.boardManage.dao.CommentRepository;
import com.onebucket.domain.boardManage.dao.LikesMapRepository;
import com.onebucket.domain.boardManage.dao.MarketPostRepository;
import com.onebucket.domain.boardManage.dto.internal.comment.GetCommentDto;
import com.onebucket.domain.boardManage.dto.postDto.PostDto;
import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.boardManage.entity.post.GroupTradePost;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.tradeManage.dao.pendingTrade.GroupTradeRepository;
import com.onebucket.global.minio.MinioRepository;
import com.onebucket.global.redis.RedisRepository;
import com.onebucket.global.utils.SecurityUtils;

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
public class GroupTradePostServiceImpl extends AbstractPostService<GroupTradePost, MarketPostRepository> {

    private final GroupTradeRepository groupTradeRepository;
    public GroupTradePostServiceImpl(MarketPostRepository repository,
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

        Member member = findMember(dto.getUserId());
        Board board = findBoard(dto.getBoardId());


        return null;
    }

    @Override
    protected PostDto.Thumbnail convertPostToThumbnailDto(GroupTradePost post) {
        return null;
    }

    @Override
    protected PostDto.Info convertPostToPostInfoDto(GroupTradePost post, List<GetCommentDto> comments) {
        return null;
    }
}
