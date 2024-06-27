package com.onebucket.domain.memberManage.service;

import com.onebucket.domain.memberManage.dto.CreateMemberRequestDTO;
import com.onebucket.domain.memberManage.dto.UpdateNickNameRequestDTO;

/**
 * <br>package name   : com.onebucket.domain.service
 * <br>file name      : MemberService
 * <br>date           : 2024-06-24
 * <pre>
 * <span style="color: white;">[description]</span>
 *
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
    void createMember(CreateMemberRequestDTO createMemberRequestDTO);

    // 일단 nickname을 수정하는 dto를 따로 만들고 비밀번호를 따로 매개변수 없이 구현해야 될듯
    void updateMember(String username, UpdateNickNameRequestDTO updateNickNameRequestDTO);
}
