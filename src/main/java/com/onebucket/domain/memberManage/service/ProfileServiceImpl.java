package com.onebucket.domain.memberManage.service;

import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.dao.ProfileRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.memberManage.domain.Profile;
import com.onebucket.domain.memberManage.dto.ReadProfileDto;
import com.onebucket.domain.memberManage.dto.UpdateProfileDto;
import com.onebucket.global.exceptionManage.customException.CommonException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.CommonErrorCode;
import com.onebucket.global.minio.MinioRepository;
import com.onebucket.global.minio.MinioInfoDto;
import com.onebucket.global.utils.EntityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

/**
 * <br>package name   : com.onebucket.domain.memberManage.service
 * <br>file name      : ProfileServiceImpl
 * <br>date           : 2024-07-03
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
 * 2024-07-03        jack8              init create
 * </pre>
 *
 */

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;
    private final MinioRepository minioRepository;

    @Value("${minio.bucketName}")
    private String bucketName;


    @Transactional
    @Override
    public void createInitProfile(Long id) {

        LocalDateTime now = LocalDateTime.now();

        Profile profile = Profile.builder()
                .id(id)
                .createAt(now)
                .updateAt(now)
                .build();

        try{
            profileRepository.save(profile);
            Member member = memberRepository.findById(id).orElseThrow(() -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));
            member.setProfile(profile);
            memberRepository.save(member);
        } catch(DataIntegrityViolationException e) {
            throw new AuthenticationException(AuthenticationErrorCode.DUPLICATE_PROFILE);
        }
    }

    @Override
    public ReadProfileDto readProfile(Long id) {
        Profile profile = getprofile(id);

        return ReadProfileDto.builder()
                .name(profile.getName())
                .birth(profile.getBirth())
                .description(profile.getDescription())
                .age(profile.getAge())
                .gender(profile.getGender())
                .email(profile.getEmail())
                .imageUrl(profile.getImageUrl())
                .createAt(profile.getCreateAt())
                .updateAt(profile.getUpdateAt())
                .build();
    }

    @Override
    public void updateProfile(Long id, UpdateProfileDto dto) {
        Profile profile = getprofile(id);

        EntityUtils.updateIfNotNull(dto.getName(), profile::setName);
        EntityUtils.updateIfNotNull(dto.getGender(), profile::setGender);
        EntityUtils.updateIfNotNull(dto.getAge(), profile::setAge);
        EntityUtils.updateIfNotNull(dto.getDescription(), profile::setDescription);
        EntityUtils.updateIfNotNull(dto.getBirth(), profile::setBirth);

        profileRepository.save(profile);
    }

    @Override
    public void updateImage(Long id, MultipartFile file) {
        Profile profile = getprofile(id);
        String url = "profile/" + id + "/" + "profile_image";

        MinioInfoDto minioDto = MinioInfoDto.builder()
                .bucketName(bucketName)
                .fileName(url)
                .fileExtension("png")
                .build();

        try {
            minioRepository.uploadFile(file, minioDto);
            profile.setUpdateAt(LocalDateTime.now());
            profile.setImageUrl(url);
            profileRepository.save(profile);
        } catch(Exception e) {
            throw new AuthenticationException(AuthenticationErrorCode.PROFILE_IMAGE_ERROR, e.getMessage());
        }
    }

    @Override
    public void updateImageToBasic(Long id) {
        Profile profile = getprofile(id);

        profile.setUpdateAt(LocalDateTime.now());
        profile.setImageUrl(null);
        profileRepository.save(profile);
    }

    @Override
    public void updateProfileEmail(String username, String email) {
        Long id = memberRepository.findIdByUsername(username).orElseThrow(
                () -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));
        Profile profile = profileRepository.findById(id).orElseThrow(
                () -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER_PROFILE));
        profile.setEmail(email);
        profileRepository.save(profile);
    }

    //TODO: don't test yet... also need to delete other image process logic.
    public String getProfileImageUrl(Long id) {

        MinioInfoDto minioDto = MinioInfoDto.builder()
                .bucketName(bucketName)
                .fileName("profile/" + id + "/" + "profile_image")
                .fileExtension("png")
                .build();

        try {
            return minioRepository.getUrl(minioDto);
        } catch (Exception e) {
            throw new CommonException(CommonErrorCode.DATA_ACCESS_ERROR);
        }

    }

    private Profile getprofile(Long id) {
        return profileRepository.findById(id).orElseThrow(
                () -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER_PROFILE)
        );
    }
}
