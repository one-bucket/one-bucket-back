package com.onebucket.global.auth.springSecurity;

import com.onebucket.domain.boardManage.dao.BoardRepository;
import com.onebucket.domain.boardManage.dao.PostRepository;
import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.boardManage.entity.post.Post;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.tradeManage.dao.pendingTrade.BaseTradeRepository;
import com.onebucket.domain.tradeManage.dao.pendingTrade.TradeRepository;
import com.onebucket.domain.tradeManage.entity.BaseTrade;
import com.onebucket.domain.universityManage.domain.University;
import com.onebucket.global.exceptionManage.customException.TradeManageException.TradeException;
import com.onebucket.global.exceptionManage.customException.boardManageException.UserBoardException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.BoardErrorCode;
import com.onebucket.global.exceptionManage.errorCode.TradeErrorCode;
import com.onebucket.global.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <br>package name   : com.onebucket.global.auth.springSecurity
 * <br>file name      : AuthorizationService
 * <br>date           : 2024-09-14
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
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorizationService {
    private final SecurityUtils securityUtils;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final BaseTradeRepository baseTradeRepository;

    public boolean isUserCanAccessBoard(Long boardId) {
        Long userId = securityUtils.getUserId();
        Member member = findMember(userId);

        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new UserBoardException(BoardErrorCode.UNKNOWN_BOARD));

        University university = member.getUniversity();

        if (university != null) {
            return university.getId().equals(board.getUniversity().getId());

        } else {
            throw new AuthenticationException(AuthenticationErrorCode.UNAUTHORIZED_ACCESS);
        }
    }

    public boolean isUserCanAccessPost(Long postId) {
        Long userId = securityUtils.getUserId();
        Member member = findMember(userId);

        Post post = findPost(postId);

        Board board = post.getBoard();

        University university = member.getUniversity();
        if (university != null) {
            return university.getId().equals(board.getUniversity().getId());

        } else {
            throw new AuthenticationException(AuthenticationErrorCode.UNAUTHORIZED_ACCESS);
        }
    }

    public boolean isUserOwnerOfPost(Long postId) {
        Long userId = securityUtils.getUserId();

        Post post = findPost(postId);

        return post.getAuthorId().equals(userId);
    }

    public boolean isUserOwnerOfTrade(Long tradeId) {
        Long userId = securityUtils.getUserId();
        Long tradeOwner = findTrade(tradeId).getOwner().getId();

        return userId.equals(tradeOwner);
    }

    private Member findMember(Long userId) {
        return memberRepository.findById(userId).orElseThrow(() ->
                new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new UserBoardException(BoardErrorCode.UNKNOWN_POST));
    }

    private BaseTrade findTrade(Long tradeId) {
        return baseTradeRepository.findById(tradeId).orElseThrow(() ->
                new TradeException(TradeErrorCode.UNKNOWN_TRADE));
    }

}
