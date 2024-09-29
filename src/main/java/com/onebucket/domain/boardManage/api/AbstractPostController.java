package com.onebucket.domain.boardManage.api;

import com.onebucket.domain.boardManage.dto.internal.board.GetBoardDto;
import com.onebucket.domain.boardManage.dto.internal.post.*;
import com.onebucket.domain.boardManage.dto.parents.PostDto;
import com.onebucket.domain.boardManage.dto.parents.ValueDto;
import com.onebucket.domain.boardManage.entity.post.Post;
import com.onebucket.domain.boardManage.service.BasePostService;
import com.onebucket.domain.boardManage.service.BoardService;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.global.exceptionManage.customException.boardManageException.UserBoardException;
import com.onebucket.global.exceptionManage.errorCode.CommonErrorCode;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseDto;
import com.onebucket.global.utils.SuccessResponseWithIdDto;
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
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 */

@RequiredArgsConstructor
public abstract class AbstractPostController<T extends Post, S extends BasePostService> {
    protected final S postService;
    protected final SecurityUtils securityUtils;
    protected final MemberService memberService;
    protected final BoardService boardService;

    @GetMapping("/list/{boardId}")
    @PreAuthorize("@authorizationService.isUserCanAccessBoard(#boardId)")
    public ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostsByBoard(@PathVariable Long boardId, Pageable pageable) {
        GetBoardDto getBoardDto = GetBoardDto.builder()
                .boardId(boardId)
                .pageable(pageable)
                .build();

        return getPostByBoardInternal(getBoardDto);
    }

    protected abstract ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostByBoardInternal(GetBoardDto getBoardDto);

    @GetMapping("/{postId}")
    @PreAuthorize("@authorizationService.isUserCanAccessPost(#postId)")
    public ResponseEntity<? extends PostDto.ResponseInfo> getPostById(@PathVariable Long postId) {
        String username = securityUtils.getCurrentUsername();
        Long userId = memberService.usernameToId(username);

        ValueDto.FindPost findPost = ValueDto.FindPost.builder()
                .postId(postId)
                .userId(userId)
                .build();

        return getPostInternal(findPost);
    }

    protected abstract ResponseEntity<? extends PostDto.ResponseInfo> getPostInternal(ValueDto.FindPost dto);

    protected void increaseViewCountInternal(ValueDto.FindPost dto) {
        postService.increaseViewCount(dto);
    }
    @DeleteMapping("/{postId}")
    public ResponseEntity<SuccessResponseWithIdDto> deletePost(@PathVariable Long postId) {
        String username = securityUtils.getCurrentUsername();
        Long userId = memberService.usernameToId(username);

        ValueDto.FindPost deletePostDto = ValueDto.FindPost.builder()
                .postId(postId)
                .userId(userId)
                .build();

        System.out.println("in time 1");
        postService.deletePost(deletePostDto);

        return ResponseEntity.ok(new SuccessResponseWithIdDto("success delete post", postId));
    }

    @PostMapping("/{postId}/like")
    @PreAuthorize("@authorizationService.isUserCanAccessPost(#postId)")
    public ResponseEntity<SuccessResponseDto> addLikes(@PathVariable Long postId) {
        String username = securityUtils.getCurrentUsername();
        Long userId = memberService.usernameToId(username);
        ValueDto.FindPost dto = ValueDto.FindPost.builder()
                .userId(userId)
                .postId(postId)
                .build();
        postService.increaseLikesCount(dto);

        return ResponseEntity.ok(new SuccessResponseDto("success add likes"));
    }

    @DeleteMapping("/{postId}/like")
    @PreAuthorize("@authorizationService.isUserCanAccessPost(#postId)")
    public ResponseEntity<SuccessResponseDto> deleteLikes(@PathVariable Long postId) {
        String username = securityUtils.getCurrentUsername();
        Long userId = memberService.usernameToId(username);
        ValueDto.FindPost dto = ValueDto.FindPost.builder()
                .userId(userId)
                .postId(postId)
                .build();
        postService.decreaseLikesCount(dto);

        return ResponseEntity.ok(new SuccessResponseDto("success delete likes"));
    }

    @PostMapping("/save/image/{postId}")
    @PreAuthorize("@authorizationService.isUserCanAccessPost(#postId)")
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








}
