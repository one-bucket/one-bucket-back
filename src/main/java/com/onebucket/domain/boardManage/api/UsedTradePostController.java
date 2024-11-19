package com.onebucket.domain.boardManage.api;

import com.onebucket.domain.boardManage.dto.postDto.PostDto;
import com.onebucket.domain.boardManage.dto.postDto.PostKeyDto;
import com.onebucket.domain.boardManage.service.BoardService;
import com.onebucket.domain.boardManage.service.postService.GroupTradePostService;
import com.onebucket.domain.boardManage.service.postService.UsedTradePostService;
import com.onebucket.domain.boardManage.service.postService.UsedTradePostServiceImpl;
import com.onebucket.domain.chatManager.service.ChatRoomService;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.tradeManage.api.UsedTradeController;
import com.onebucket.domain.tradeManage.service.UsedTradeService;
import com.onebucket.global.utils.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <br>package name   : com.onebucket.domain.boardManage.api
 * <br>file name      : UsedTradePostController
 * <br>date           : 2024-11-19
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
@RestController
@RequestMapping("/used-post")
public class UsedTradePostController extends AbstractPostController<UsedTradePostService> {
    private final UsedTradeService usedTradeService;
    private final ChatRoomService chatRoomService;

    public UsedTradePostController(UsedTradePostService postService,
                                   SecurityUtils securityUtils,
                                   MemberService memberService,
                                   BoardService boardService,
                                   UsedTradeService usedTradeService,
                                   ChatRoomService chatRoomService) {
        super(postService, securityUtils, memberService, boardService);
        this.usedTradeService = usedTradeService;
        this.chatRoomService = chatRoomService;
    }

    @Override
    protected ResponseEntity<? extends PostDto.ResponseInfo> getPostInternal(PostKeyDto.UserPost dto) {
        return null;
    }

    @Override
    protected ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostsBySearchInternal(PostKeyDto.SearchPage dto) {
        return null;
    }

    @Override
    protected ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostByBoardInternal(PostKeyDto.BoardPage dto) {
        return null;
    }

    @Override
    protected ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostByAuthorInternal(PostKeyDto.AuthorPage dto) {
        return null;
    }
}
