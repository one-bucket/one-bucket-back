package com.onebucket.domain.chatManager.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.chatManager.dto
 * <br>file name      : ChatRoomDto
 * <br>date           : 10/16/24
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
public class ChatRoomDto {


    @Builder
    @Getter
    public static class MemberInfo {
        private String nickname;
        private LocalDateTime joinedAt;
        private String role;
    }

    @Builder
    @Getter
    public static class CreateRoom {
        private String name;
        private Long memberId;
        private Long tradeId;

    }

    @SuperBuilder
    @Getter
    public static class GetTradeInfo {
        private String authorNickname;
        private String item;
        private Long price;
        private String imageUrl;
        private String tag;
        private Long count;

        private List<MemberInfo> memberList;
    }
}