package com.onebucket.domain.memberManage.service;

import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.memberManage.dto.CreateMemberRequestDto;
import com.onebucket.domain.memberManage.dto.UpdateNicknameRequestDto;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.RegisterException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * <br>package name   : com.onebucket.domain.service
 * <br>file name      : MemberServiceImpl
 * <br>date           : 2024-06-24
 * <pre>
 * <span style="color: white;">[description]</span>
 * password는 update를 추가하되, 어플리케이션 단에서 직접 입력하는게 아니라 서버에서 랜덤으로 문자열을 생성하여 컨트롤러로 반환하고, 이를 디코딩하여 저장하는 방식으로 만들어야됨.
 * 즉 password는 service단에서 매개변수를 받아서 update하는게 아니라 매개변수 없이 임의의 문자열을 집어넣고 해당 문자열을 return하는 메서드로 구현하면 될듯
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 * memberService.createMember(createMemberRequestDto);
 * memberService.updateMember(username, updateNickNameRequestDto);
 * } </pre>
 * <pre>
 * modified log :
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-06-24        SeungHoon              init create
 * </pre>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;


    @Override
    public void createMember(CreateMemberRequestDto createMemberRequestDTO) {
        Member member = Member.builder()
                .username(createMemberRequestDTO.getUsername())
                .password(createMemberRequestDTO.getPassword())
                .nickname(createMemberRequestDTO.getNickname())
                .build();
        try {
            memberRepository.save(member);
        } catch(DataIntegrityViolationException e) {
            throw new RegisterException(AuthenticationErrorCode.DUPLICATE_USER,
                    "username or nickname already exist.");
        }
    }

    @Override
    public void updateMember(String username, UpdateNicknameRequestDto updateNicknameRequestDTO) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new EntityNotFoundException("Member not found"));

        member.setNickname(updateNicknameRequestDTO.getNickname());
        memberRepository.save(member);
    }
}
