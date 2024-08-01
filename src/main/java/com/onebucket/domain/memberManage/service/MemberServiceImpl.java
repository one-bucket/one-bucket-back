package com.onebucket.domain.memberManage.service;

import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.memberManage.dto.CreateMemberRequestDto;
import com.onebucket.domain.memberManage.dto.NicknameRequestDto;
import com.onebucket.domain.memberManage.dto.ReadMemberInfoDto;
import com.onebucket.domain.universityManage.domain.University;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
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
 *
 * @tested true
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RandomStringUtils randomStringUtils;



    @Override
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
            throw new AuthenticationException(AuthenticationErrorCode.DUPLICATE_USER,
                    "username or nickname already exist.");
        }
    }

    @Override
    public ReadMemberInfoDto readMember(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));
        String universityName = Optional.ofNullable(member.getUniversity())
                .map(University::getName)
                .orElse("null");
        return new ReadMemberInfoDto(username, member.getNickname(), universityName);
    }

    @Override
    public void updateMember(String username, NicknameRequestDto nicknameRequestDTO) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));

        member.setNickname(nicknameRequestDTO.getNickname());
        try {
            memberRepository.save(member);
        } catch(DataIntegrityViolationException e) {
            throw new AuthenticationException(AuthenticationErrorCode.DUPLICATE_USER, "nickname duplicate");
        }
    }

    @Override
    public void quitMember(String username) {
//        Member member = memberRepository.findByUsername(username)
//                .orElseThrow(() -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));
        memberRepository.deleteByUsername(username);
//        member.setEnable(false);
//        memberRepository.save(member);
    }

    @Override
    public String changePassword(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));

        String newPassword = randomStringUtils.generateRandomStr(15);
        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
        return newPassword;
    }

    @Override
    public String changePassword(String username, String newPassword) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));

        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
        return newPassword;
    }


    @Override
    public Long usernameToId(String username) {
        return memberRepository.findIdByUsername(username)
                .orElseThrow(() -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));
    }

    @Override
    public String idToNickname(Long id) {
        return memberRepository.findNicknameById(id)
                .orElseThrow(() -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));
    }

    @Override
    public University usernameToUniversity(String username) {
        University university = University.builder()
                .name("null")
                .email("null")
                .address("null")
                .build();
        return Optional.ofNullable(memberRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER)).getUniversity())
                .orElse(university);
    }


}
