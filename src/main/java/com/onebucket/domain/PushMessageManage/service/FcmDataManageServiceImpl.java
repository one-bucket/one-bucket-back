package com.onebucket.domain.PushMessageManage.service;

import com.onebucket.domain.PushMessageManage.dto.TokenDto;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <br>package name   : com.onebucket.domain.PushMessageManage.service
 * <br>file name      : FcmDataManageServiceImpl
 * <br>date           : 2024-11-03
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
public class FcmDataManageServiceImpl {

    private final MemberRepository memberRepository;

    @Transactional
    public void saveDeviceToken(TokenDto.Info dto) {
        Member member = findMember(dto.getUserId());

        member.setDeviceToken(dto.getToken());
        memberRepository.save(member);
    }

    public void saveMessageLog

    private Member findMember(Long userId) {
        return memberRepository.findById(userId).orElseThrow(() ->
                new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));
    }
}
