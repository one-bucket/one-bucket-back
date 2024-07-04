package com.onebucket.domain.memberManage.service;

import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.memberManage.dto.CreateMemberRequestDto;
import com.onebucket.domain.memberManage.dto.UpdateNicknameRequestDto;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.RegisterException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.utils.RandomStringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * <br>package name   : com.onebucket.domain.service
 * <br>file name      : MemberServiceImpl
 * <br>date           : 2024-06-24
 * <pre>
 * <span style="color: white;">[description]</span>
 * {@link MemberService}
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RandomStringUtils randomStringUtils;



    @Override
    @Transactional
    public Long createMember(CreateMemberRequestDto createMemberRequestDto) {

        Member member = Member.builder()
                .username(createMemberRequestDto.getUsername())
                .password(passwordEncoder.encode(createMemberRequestDto.getPassword()))
                .nickname(createMemberRequestDto.getNickname())
                .build();
        try {
            Member newMember = memberRepository.save(member);
            return newMember.getId();
        } catch(DataIntegrityViolationException e) {
            throw new RegisterException(AuthenticationErrorCode.DUPLICATE_USER,
                    "username or nickname already exist.");
        }
    }

    @Override
    @Transactional
    public void updateMember(String username, UpdateNicknameRequestDto updateNicknameRequestDTO) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new RegisterException(AuthenticationErrorCode.UNKNOWN_USER));

        member.setNickname(updateNicknameRequestDTO.getNickname());
        memberRepository.save(member);
    }
    // TODO: test case

    @Override
    @Transactional
    public String changePassword(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new RegisterException(AuthenticationErrorCode.UNKNOWN_USER));

        String newPassword = randomStringUtils.generateRandomStr(15);

        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
        return newPassword;
    }

    @Override
    @Transactional
    public String changePassword(String username, String newPassword) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new RegisterException(AuthenticationErrorCode.UNKNOWN_USER));

        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
        return newPassword;
    }


    @Override
    public Long usernameToId(String username) {
        return memberRepository.findIdByUsername(username)
                .orElseThrow(() -> new RegisterException(AuthenticationErrorCode.UNKNOWN_USER));
    }
}
