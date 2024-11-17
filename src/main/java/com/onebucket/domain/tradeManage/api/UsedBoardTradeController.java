package com.onebucket.domain.tradeManage.api;

import com.onebucket.domain.chatManager.service.ChatRoomService;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.tradeManage.service.UsedTradeService;
import com.onebucket.global.utils.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.api
 * <br>file name      : UsedBoardTradeController
 * <br>date           : 11/15/24
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
@RequestMapping("/used-trade")
public class UsedBoardTradeController extends AbstractTradeController<UsedTradeService> {

    private final ChatRoomService chatRoomService;

    public UsedBoardTradeController(UsedTradeService tradeService,
                                    SecurityUtils securityUtils,
                                    MemberService memberService,
                                    ChatRoomService chatRoomService) {
        super(tradeService, securityUtils, memberService);
        this.chatRoomService = chatRoomService;
    }

    @PostMapping("/chat")
    public ResponseEntity<?> createNewChatRoom() {
        chatRoomService.
    }


}
