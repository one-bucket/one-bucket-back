package com.onebucket.domain.PushMessageManage.service;

import com.onebucket.domain.PushMessageManage.Entity.DeviceToken;
import com.onebucket.domain.PushMessageManage.Entity.PushMessageLog.PushMessageLog;
import com.onebucket.domain.PushMessageManage.JpaDao.DeviceTokenRepository;
import com.onebucket.domain.PushMessageManage.MongoDao.PushMessageLogRepository;
import com.onebucket.domain.PushMessageManage.dto.MessageLogDto;
import com.onebucket.domain.PushMessageManage.dto.TokenDto;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

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
public class FcmDataManageServiceImpl implements FcmDataManageService {

    private final MemberRepository memberRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private final PushMessageLogRepository pushMessageLogRepository;

    @Transactional
    @Override
    @CacheEvict(value = "userTokenCache", key = "#dto.userId")
    public void updateDeviceToken(TokenDto.Info dto) {
        String token = dto.getToken();
        Member member = findMember(dto.getUserId());

        Optional<DeviceToken> deviceToken = deviceTokenRepository.findByMember(member);
        if(deviceToken.isEmpty()) {
            DeviceToken newToken = DeviceToken.builder()
                    .deviceToken(token)
                    .member(member)
                    .build();

            newToken.dateUpdate();

            deviceTokenRepository.save(newToken);
        } else {
            DeviceToken findToken = deviceToken.get();
            findToken.dateUpdate();

            if(!findToken.getDeviceToken().equals(dto.getToken())) {
                findToken.setDeviceToken(token);
            }

            deviceTokenRepository.save(findToken);
        }
    }

    @Transactional
    @Override
    public void saveMessageLog(MessageLogDto.Info dto) {
        LocalDateTime now = LocalDateTime.now();
        PushMessageLog pushMessageLog = PushMessageLog.builder()
                .title(dto.getTitle())
                .body(dto.getBody())
                .image(dto.getImage())
                .sendAt(now)
                .build();

        pushMessageLogRepository.save(pushMessageLog);
    }

    @Transactional(readOnly = true)
    @Override
    @Cacheable(value = "userTokenCache")
    public String getTokensByUserId(Long userId) {
        DeviceToken deviceToken = deviceTokenRepository.findByMemberId(userId).orElse(null);
        if(deviceToken != null) {
            return deviceToken.getDeviceToken();
        }
        return null;

    }

    private Member findMember(Long userId) {
        return memberRepository.findById(userId).orElseThrow(() ->
                new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));
    }

}
