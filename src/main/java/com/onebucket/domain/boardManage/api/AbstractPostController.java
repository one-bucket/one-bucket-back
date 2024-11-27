package com.onebucket.domain.boardManage.api;

import com.onebucket.domain.boardManage.dto.internal.post.*;
import com.onebucket.domain.boardManage.dto.postDto.PostDto;
import com.onebucket.domain.boardManage.dto.postDto.PostKeyDto;
import com.onebucket.domain.boardManage.service.postService.BasePostService;
import com.onebucket.domain.boardManage.service.BoardService;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.global.exceptionManage.customException.boardManageException.UserBoardException;
import com.onebucket.global.exceptionManage.errorCode.CommonErrorCode;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseDto;
import com.onebucket.global.utils.SuccessResponseWithIdDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.api
 * <br>file name      : AbstractPostController
 * <br>date           : 2024-09-22
 * <pre>
 * <span style="color: white;">[description]</span>
 * GET "/list/{boardId} : post 리스트
 * GET "/{postId} : 단일 post 정보
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 */

@RequiredArgsConstructor
public abstract class AbstractPostController<S extends BasePostService> {
    protected final S postService;
    protected final SecurityUtils securityUtils;
    protected final MemberService memberService;
    protected final BoardService boardService;

    @GetMapping("/list/{boardId}")
    @PreAuthorize("@authorizationService.isUserCanAccessBoard(#boardId)")
    public ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostsByBoard(@PathVariable Long boardId, Pageable pageable) {
        PostKeyDto.BoardPage boardPage = PostKeyDto.BoardPage.builder()
                .boardId(boardId)
                .pageable(pageable)
                .build();

        return getPostByBoardInternal(boardPage);
    }

    @GetMapping("/{postId}")
    @PreAuthorize("@authorizationService.isUserCanAccessPost(#postId)")
    public ResponseEntity<? extends PostDto.ResponseInfo> getPostById(@PathVariable Long postId) {
        Long userId = securityUtils.getUserId();

        PostKeyDto.UserPost userPost = PostKeyDto.UserPost.builder()
                .postId(postId)
                .userId(userId)
                .build();

        return getPostInternal(userPost);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostBySearch(
            @RequestParam Long boardId,
            @RequestParam Integer option,
            @RequestParam String keyword,
            Pageable pageable) {

        PostKeyDto.SearchPage searchPage = PostKeyDto.SearchPage.builder()
                .boardId(boardId)
                .option(option)
                .keyword(keyword)
                .pageable(pageable)
                .build();

        return getPostsBySearchInternal(searchPage);
    }

    @GetMapping("/list/my/{boardId}")
    public ResponseEntity<Page<? extends PostDto.Thumbnail>> getAuthorsPost(@PathVariable Long boardId,
                                                                            Pageable pageable) {
        Long userId = securityUtils.getUserId();
        PostKeyDto.AuthorPage authorPage = PostKeyDto.AuthorPage.builder()
                .boardId(boardId)
                .pageable(pageable)
                .userId(userId)
                .build();

        return getPostByAuthorInternal(authorPage);
    }


    @GetMapping("/list/likes")
    public ResponseEntity<Page<? extends PostDto.Thumbnail>> getLikesPost(Pageable pageable) {
        Long userId = securityUtils.getUserId();
        PostKeyDto.AuthorPage authorPage = PostKeyDto.AuthorPage.builder()
                .userId(userId)
                .pageable(pageable)
                .build();
        Page<PostDto.Thumbnail> response = postService.getPostByLikes(authorPage)
                .map(this::convertInternalThumbnailToThumbnail);

        return ResponseEntity.ok(response);
    }



    @DeleteMapping("/{postId}")
    @PreAuthorize("@authorizationService.isUserOwnerOfPost(#postId)")
    public ResponseEntity<SuccessResponseWithIdDto> deletePost(@PathVariable Long postId) {

        PostKeyDto.PostKey postKey = PostKeyDto.PostKey.builder()
                .postId(postId)
                .build();

        postService.deletePost(postKey);

        return ResponseEntity.ok(new SuccessResponseWithIdDto("success delete post", postId));
    }



    // 좋아요 관련 로직
    @PostMapping("/{postId}/like")
    @PreAuthorize("@authorizationService.isUserCanAccessPost(#postId)")
    public ResponseEntity<SuccessResponseDto> addLikes(@PathVariable Long postId) {
        Long userId = securityUtils.getUserId();
        PostKeyDto.UserPost dto = PostKeyDto.UserPost.builder()
                .userId(userId)
                .postId(postId)
                .build();
        postService.increaseLikesCount(dto);

        return ResponseEntity.ok(new SuccessResponseDto("success add likes"));
    }

    @DeleteMapping("/{postId}/like")
    @PreAuthorize("@authorizationService.isUserCanAccessPost(#postId)")
    public ResponseEntity<SuccessResponseDto> deleteLikes(@PathVariable Long postId) {
        Long userId = securityUtils.getUserId();
        PostKeyDto.UserPost dto = PostKeyDto.UserPost.builder()
                .userId(userId)
                .postId(postId)
                .build();
        postService.decreaseLikesCount(dto);

        return ResponseEntity.ok(new SuccessResponseDto("success delete likes"));
    }

    //이미지 관련 로직
    @PostMapping("/save/image/{postId}")
    @PreAuthorize("@authorizationService.isUserOwnerOfPost(#postId)")
    public ResponseEntity<SuccessResponseWithIdDto> saveImage(
            @PathVariable Long postId,
            @RequestParam("file") List<MultipartFile> files
            ) {
        Long index = 0L;
        for(MultipartFile file : files) {
            String originFileName = file.getOriginalFilename();
            String fileName = getFileNameWithoutExtension(originFileName);
            String extension = getFileExtension(originFileName);
            SaveImageDto dto = SaveImageDto.builder()
                    .postId(postId)
                    .imageName(fileName)
                    .fileExtension(extension)
                    .build();
            postService.saveImage(file, dto);
            index++;
        }
        return ResponseEntity.ok(new SuccessResponseWithIdDto("success save images", index));
    }

    @PostMapping("/update/image/{type}/{postId}")
    @PreAuthorize("@authorizationService.isUserOwnerOfPost(#postId)")
    public ResponseEntity<?> updateImage(
            @PathVariable String type,
            @PathVariable Long postId,
            @RequestParam(value = "file", required = false) List<MultipartFile> files
    ) {
        Long index = 0L;
        if(type.equals("add")) {
            if(files == null || files.isEmpty()) {
                return ResponseEntity.badRequest().body("파일이 필요합니다.");
            }
            index += addImageData(postId, files);
        } else if(type.equals("delete")) {
            initImageData(postId);
        } else if(type.equals("update")) {
            if(files == null || files.isEmpty()) {
                return ResponseEntity.badRequest().body("파일이 필요합니다.");
            }
            initImageData(postId);
            index = addImageData(postId, files);
        }

        return ResponseEntity.ok(new SuccessResponseWithIdDto("success save images", index));
    }


    private void initImageData(Long postId) {
        postService.deleteImageOnPost(postId);
    }

    private Long addImageData(Long postId, List<MultipartFile> files) {
        Long index = 0L;
        for(MultipartFile file : files) {
            String originFileName = file.getOriginalFilename();
            String fileName = getFileNameWithoutExtension(originFileName);
            String extension = getFileExtension(originFileName);
            SaveImageDto dto = SaveImageDto.builder()
                    .postId(postId)
                    .imageName(fileName)
                    .fileExtension(extension)
                    .build();
            postService.saveImage(file, dto);
            index++;
        }

        return index;
    }

    protected PostDto.Thumbnail convertInternalThumbnailToThumbnail(PostDto.InternalThumbnail dto) {
        Long commentCount = postService.getCommentCount(dto.getPostId());
        Long likes = dto.getLikes() + postService.getLikesInRedis(dto.getPostId());

        PostDto.Thumbnail response = PostDto.Thumbnail.of(dto);
        response.setCommentsCount(commentCount);
        response.setLikes(likes);

        return response;
    }

    private String getFileNameWithoutExtension(String fileName) {
        if(fileName == null) {
            throw new UserBoardException(CommonErrorCode.ILLEGAL_ARGUMENT, "file name is null");
        }
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    // 파일 확장자 추출 메서드
    private String getFileExtension(String fileName) {
        if(fileName == null) {
            throw new UserBoardException(CommonErrorCode.ILLEGAL_ARGUMENT, "file name is null");
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
    protected void increaseViewCountInternal(PostKeyDto.UserPost dto) {
        postService.increaseViewCount(dto);
    }

    protected PostDto.ResponseInfo convertInfoToResponse(PostDto.Info info, PostKeyDto.UserPost dto) {

        //likes 정보 불러오기
        Long savedInRedisLikes = postService.getLikesInRedis(info.getPostId());
        Long likes = info.getLikes() + savedInRedisLikes;
        boolean isUserAlreadyLikes = postService.isUserLikesPost(dto);

        //변환 및 삽입
        PostDto.ResponseInfo response = PostDto.ResponseInfo.of(info);
        response.setLikes(likes);
        response.setUserAlreadyLikes(isUserAlreadyLikes);

        //조회수 증가
        increaseViewCountInternal(dto);

        return response;
    }

    protected abstract ResponseEntity<? extends PostDto.ResponseInfo> getPostInternal(PostKeyDto.UserPost dto);
    protected abstract ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostsBySearchInternal(PostKeyDto.SearchPage dto);
    protected abstract ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostByBoardInternal(PostKeyDto.BoardPage dto);
    protected abstract ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostByAuthorInternal(PostKeyDto.AuthorPage dto);





}
