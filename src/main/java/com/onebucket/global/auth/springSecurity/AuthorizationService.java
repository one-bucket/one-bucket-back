package com.onebucket.global.auth.springSecurity;

import com.onebucket.domain.boardManage.dao.BoardRepository;
import com.onebucket.domain.boardManage.dao.PostRepository;
import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.boardManage.entity.post.Post;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.universityManage.domain.University;
import com.onebucket.global.exceptionManage.customException.boardManageException.UserBoardException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.BoardErrorCode;
import com.onebucket.global.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
public class AuthorizationService {
    private final SecurityUtils securityUtils;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;

    public boolean isUserCanAccessBoard(Long boardId) {
        String username = securityUtils.getCurrentUsername();
        Member member = memberRepository.findByUsername(username).orElseThrow(() ->
                new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));

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
        String username = securityUtils.getCurrentUsername();
        Member member = memberRepository.findByUsername(username).orElseThrow(() ->
                new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));

        Post post = postRepository.findById(postId).orElseThrow(() ->
                new UserBoardException(BoardErrorCode.UNKNOWN_POST));

        Board board = post.getBoard();

        University university = member.getUniversity();
        if (university != null) {
            return university.getId().equals(board.getUniversity().getId());

        } else {
            throw new AuthenticationException(AuthenticationErrorCode.UNAUTHORIZED_ACCESS);
        }
    }

}
