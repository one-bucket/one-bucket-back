package com.onebucket.domain.memberManage.service;

import com.onebucket.domain.memberManage.dto.CreateMemberRequestDto;
import com.onebucket.domain.memberManage.dto.NicknameRequestDto;
import com.onebucket.domain.memberManage.dto.ReadMemberInfoDto;
import com.onebucket.domain.memberManage.dto.internal.SetUniversityDto;
import com.onebucket.domain.universityManage.domain.University;

/**
 * <br>package name   : com.onebucket.domain.service
 * <br>file name      : MemberService
 * <br>date           : 2024-06-24
 * <br>TODO: add password update method that make random string and save.
 * <br>TODO: add deleteMember method.
 * <br>TODO: add readMember method.
 * <pre>
 * <span style="color: white;">[description]</span>
 * CRUD algorithm of {@link com.onebucket.domain.memberManage.domain.Member Member}.
 * Two way of changing password, by random string and user set.
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
    Long createMember(CreateMemberRequestDto createMemberRequestDTO);
    ReadMemberInfoDto readMember(String username);
    void updateMember(String username, NicknameRequestDto nickNameRequestDTO);
    void quitMember(String username);
    Long usernameToId(String username);
    String changePassword(String username);
    String changePassword(String username, String newPassword);

    void setUniversity(SetUniversityDto dto);

    String idToNickname(Long id);
    University usernameToUniversity(String username);

}
