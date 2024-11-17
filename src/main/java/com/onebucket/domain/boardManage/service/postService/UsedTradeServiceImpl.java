package com.onebucket.domain.boardManage.service.postService;

import com.onebucket.domain.boardManage.dao.BoardRepository;
import com.onebucket.domain.boardManage.dao.CommentRepository;
import com.onebucket.domain.boardManage.dao.LikesMapRepository;
import com.onebucket.domain.boardManage.dao.postRepository.UsedTradePostRepository;
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

/**
 * <br>package name   : com.onebucket.domain.boardManage.service.postService
 * <br>file name      : UsedTradeServiceImpl
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
public class UsedTradeServiceImpl extends AbstractPostService<UsedTradePost, UsedTradePostRepository>
implements UsedTradePostService {
    private final UsedTradeRepository usedTradeRepository;

    public UsedTradeServiceImpl(UsedTradePostRepository repository,
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
    protected PostDto.InternalThumbnail setThumbnailForOtherInfo(PostDto.InternalThumbnail dto, UsedTradePost post) {
        UsedTradePostDto.InternalThumbnail usedTradePostInfo = (UsedTradePostDto.InternalThumbnail) dto;
        usedTradePostInfo.setTrade(post.getUsedTradeId());

        return usedTradePostInfo;
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
