package com.onebucket.domain.memberManage.service;

import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.memberManage.dto.CreateMemberRequestDto;
import com.onebucket.domain.memberManage.dto.NicknameRequestDto;
import com.onebucket.domain.memberManage.dto.ReadMemberInfoDto;
import com.onebucket.domain.memberManage.dto.internal.SetPasswordDto;
import com.onebucket.domain.memberManage.dto.internal.SetUniversityDto;
import com.onebucket.domain.universityManage.dao.UniversityRepository;
import com.onebucket.domain.universityManage.domain.University;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.customException.universityManageException.UniversityException;
import com.onebucket.global.exceptionManage.customException.verificationException.VerificationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.UniversityErrorCode;
import com.onebucket.global.exceptionManage.errorCode.VerificationErrorCode;
import com.onebucket.global.utils.RandomStringUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
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
    private final UniversityRepository universityRepository;


    /**
     * {@link CreateMemberRequestDto} 를 받아 member 테이블에 정보를 입력한다.
     * 만약 이미 존재하는 username 혹은 nickname 이면 {@link AuthenticationException} 을 뱉는다.
     *
     * @param createMemberRequestDto username, password, nickname 정보를 가진다.
     * @return 사용자의 id를 반환한다.
     */
    @Override
    public Long createMember(CreateMemberRequestDto createMemberRequestDto) {



        Member member = Member.builder()
                .username(createMemberRequestDto.getUsername())
                .password(passwordEncoder.encode(createMemberRequestDto.getPassword()))
                .nickname(createMemberRequestDto.getNickname())
                .university(saveNullUniv())
                .build();
        member.addRoles("GUEST");
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
        Member member = findMember(username);
        String universityName = Optional.ofNullable(member.getUniversity())
                .map(University::getName)
                .orElse("null");
        return new ReadMemberInfoDto(username, member.getNickname(), universityName);
    }

    @Override
    public void updateMember(String username, NicknameRequestDto nicknameRequestDTO) {
        Member member = findMember(username);
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

    /**
     * 임의의 문자열로 비밀번호 초기화하기
     * @param username
     * @return
     */
    @Override
    public String initPassword(String username) {
        Member member = findMember(username);
        String newPassword = randomStringUtils.generateRandomStr(15);
        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
        return newPassword;
    }

    @Override
    public String changePassword(SetPasswordDto dto) {
        Member member = findMember(dto.getUsername());
        if (!passwordEncoder.matches(dto.getOldPassword(), member.getPassword())) {
            throw new VerificationException(VerificationErrorCode.INVALID_PASSWORD);
        }

        String newPassword = passwordEncoder.encode(dto.getNewPassword());
        member.setPassword(newPassword);
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

        University university = Optional.ofNullable(memberRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER)).getUniversity())
                .orElseThrow(() -> new UniversityException(UniversityErrorCode.NOT_EXIST_UNIVERSITY));

        if(university.getName().equals("null")) {
            throw new UniversityException(UniversityErrorCode.NOT_EXIST_UNIVERSITY);
        }
        return university;
    }

    @Transactional
    @Override
    public void setUniversity(SetUniversityDto dto) {
        Member member = findMember(dto.getUsername());
        University university = universityRepository.findByName(dto.getUniversity()).orElseThrow(() ->
                new UniversityException(UniversityErrorCode.NOT_EXIST_UNIVERSITY));

        member.setUniversity(university);
        memberRepository.save(member);
    }


    private University saveNullUniv() {

        Optional<University> university = universityRepository.findByName("null");
        if(university.isPresent()) {
            return university.get();
        } else {
            University newUniv = University.builder()
                    .name("null")
                    .email("null")
                    .address("null")
                    .build();
            return universityRepository.save(newUniv);

        }
    }

    private Member findMember(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(()-> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));
    }
}
