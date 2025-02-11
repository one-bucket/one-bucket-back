package com.onebucket.domain.boardManage.api;

import com.onebucket.domain.PushMessageManage.dto.PushMessageDto;
import com.onebucket.domain.PushMessageManage.dto.PushMessageType;
import com.onebucket.domain.PushMessageManage.service.FcmDataManageService;
import com.onebucket.domain.PushMessageManage.service.FirebaseCloudMessageService;
import com.onebucket.domain.boardManage.dto.internal.comment.CreateCommentDto;
import com.onebucket.domain.boardManage.dto.postDto.PostKeyDto;
import com.onebucket.domain.boardManage.dto.request.RequestCreateCommentDto;
import com.onebucket.domain.boardManage.service.postService.PostService;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.api
 * <br>file name      : CommentController
 * <br>date           : 2024-08-15
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
 * 2024-08-15        jack8              init create
 * </pre>
 */

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final SecurityUtils securityUtils;
    private final PostService postService;
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final FcmDataManageService fcmDataManageService;

    @PostMapping
    public ResponseEntity<SuccessResponseDto> createComment(@RequestBody @Valid RequestCreateCommentDto dto) {
        String username = securityUtils.getCurrentUsername();

        CreateCommentDto createCommentDto = CreateCommentDto.builder()
                .postId(dto.getPostId())
                .parentCommentId(dto.getParentCommentId())
                .text(dto.getText())
                .username(username)
                .build();

        List<Long> alarmIds = postService.addCommentToPost(createCommentDto);
        List<String> tokens = new ArrayList<>();
        for(Long alarmId : alarmIds) {
            String token;
            if((token = fcmDataManageService.getTokensByUserId(alarmId)) != null) {
                System.out.println("token exist");
                tokens.add(token);
            }
        }

        PostKeyDto.PostKey findPost = PostKeyDto.PostKey.builder()
                .postId(dto.getPostId())
                .build();
        String postTitle = postService.getPost(findPost).getTitle();

        String shortenText = dto.getText();
        if(dto.getText() != null && dto.getText().length() > 20) {
            shortenText = dto.getText().substring(0, 20) + "...";
        }
        if(postTitle != null && postTitle.length() > 20) {
            postTitle = postTitle.substring(0,10) + "...";
        }
        PushMessageDto.Tokens pushMessageDto = PushMessageDto.Tokens.builder()
                .body(shortenText)
                .title(postTitle + "에 댓글이 달렸습니다.")
                .type(PushMessageType.COMMENT)
                .id(dto.getPostId().toString())
                .tokens(tokens)
                .build();

        firebaseCloudMessageService.sendMessageToToken(pushMessageDto);
        return ResponseEntity.ok(new SuccessResponseDto("success create comment"));

    }
}
