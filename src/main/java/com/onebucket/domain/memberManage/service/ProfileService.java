package com.onebucket.domain.memberManage.service;

import com.onebucket.domain.memberManage.dto.ReadProfileDto;
import com.onebucket.domain.memberManage.dto.UpdateProfileDto;
import com.onebucket.domain.memberManage.dto.internal.SetEmailDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * <br>package name   : com.onebucket.domain.memberManage.service
 * <br>file name      : ProfileService
 * <br>date           : 2024-07-02
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
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-07-02        jack8              init create
 * </pre>
 */
public interface ProfileService {
    void createInitProfile(Long id);

    ReadProfileDto readProfile(Long id);
    byte[] readProfileImage(Long id);

    void updateProfile(Long id, UpdateProfileDto dto);
    void updateProfileEmail(Long id, SetEmailDto dto);
    void updateImage(Long id, MultipartFile file);
    void updateImageToBasic(Long id);

    String getImageUrl(Long id);

}
