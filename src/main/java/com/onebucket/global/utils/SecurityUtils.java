package com.onebucket.global.utils;

import com.onebucket.domain.boardManage.dao.BoardRepository;
import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.global.exceptionManage.customException.boardManageException.BoardManageException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.MemberManageException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.BoardErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * <br>package name   : com.onebucket.domain.memberManage.service
 * <br>file name      : SecurityUtils
 * <br>date           : 2024-07-01
 * <pre>
 * <span style="color: white;">[description]</span>
 * Security utility class. For get username from SecurityContextHolder, or authorities
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 * String username = securityUtils.getCurrentUsername();
 *
 * } </pre>
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-07-01        jack8              init create
 * </pre>
 */
@Component
@RequiredArgsConstructor
public class SecurityUtils {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public String getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication.getName() == null) {
            throw new AuthenticationException(AuthenticationErrorCode.NON_EXIST_AUTHENTICATION,
                    "Not exist Authentication in ContextHolder");
        }
        return authentication.getName();
    }

    public void isUserUniversityMatchingBoard(String username, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new BoardManageException(BoardErrorCode.UNKNOWN_BOARD));

        isUserUniversityMatchingBoard(username, board);
    }

    public void isUserUniversityMatchingBoard(String username, Board board) {
        Long boardUnivId = board.getUniversity().getId();

        Member member = memberRepository.findByUsername(username).orElseThrow(() ->
                new MemberManageException(AuthenticationErrorCode.UNKNOWN_USER,
                        "can't find member while matching university"));

        Long memberUnivId = member.getUniversity().getId();

        if(boardUnivId.equals(memberUnivId)) {
            return;
        } else {
            throw new AuthenticationException(AuthenticationErrorCode.INVALID_SUBMIT);
        }

    }

    public void isUserUniversityMatchingBoard(Long univId, Long boardId) {
        Long boardUnivId = boardRepository.findById(boardId).orElseThrow(() ->
                new BoardManageException(BoardErrorCode.UNKNOWN_BOARD)).getUniversity().getId();

        if(univId.equals(boardUnivId)) {
            return;
        } else {
            throw new AuthenticationException(AuthenticationErrorCode.INVALID_SUBMIT);
        }
    }

    public void isUserUniversityMatchingBoard(Long univId, Board board) {
        Long boardUnivId = board.getUniversity().getId();

        if(univId.equals(boardUnivId)) {
            return;
        } else {
            throw new AuthenticationException(AuthenticationErrorCode.INVALID_SUBMIT);
        }
    }

    public Long getUnivId(String username) {
        return memberRepository.findByUsername(username).orElseThrow(() ->
                new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER))
                .getUniversity().getId();

    }

    public Member getMember(String username) {
        return memberRepository.findByUsername(username).orElseThrow(() ->
                new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));
    }
}
