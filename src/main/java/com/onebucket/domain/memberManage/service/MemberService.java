package com.onebucket.domain.memberManage.service;

import com.onebucket.domain.memberManage.dto.CreateMemberRequestDto;
import com.onebucket.domain.memberManage.dto.UpdateNicknameRequestDto;

/**
 * <br>package name   : com.onebucket.domain.service
 * <br>file name      : MemberService
 * <br>date           : 2024-06-24
 * <br>TODO: add password update method that make random string and save.
 * <br>TODO: add deleteMember method.
 * <br>TODO: add readMember method.
 * <pre>
 * <span style="color: white;">[description]</span>
 * Interface of {@link MemberServiceImpl}.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 * <pre>
 * modified log :
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-06-24     SeungHoon              init create
 * </pre>
 */
public interface MemberService {
    void createMember(CreateMemberRequestDto createMemberRequestDTO);

    // 일단 nickname을 수정하는 dto를 따로 만들고 비밀번호를 따로 매개변수 없이 구현해야 될듯
    void updateMember(String username, UpdateNicknameRequestDto updateNickNameRequestDTO);
}
