package com.onebucket.domain.chatManager.dto;

import com.onebucket.domain.chatManager.entity.ChatRoomMember;
import com.onebucket.domain.tradeManage.entity.PendingTrade;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;
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
        private Long id;
        private String nickname;
        private LocalDateTime joinedAt;
        private String role;

        public static MemberInfo of(ChatRoomMember entity) {
            return MemberInfo.builder()
                    .id(entity.getMember().getId())
                    .nickname(entity.getMember().getNickname())
                    .joinedAt(entity.getJoinedAt())
                    .role(entity.getRole())
                    .build();
        }

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatRoomInfo {
        private String roomId;
        private String roomName;
        private Long memberCount;
        private Long stackMessage;
        private String recentMessage;
        private Date recentMessageTime;
    }

    @SuperBuilder
    @Getter
    @NoArgsConstructor
    public static class InfoAfterTime {
        private String roomId;
        private Date timestamp;

        public static InfoAfterTime of(RequestInfoAfterTime dto) {
            return InfoAfterTime.builder()
                    .roomId(dto.getRoomId())
                    .timestamp(dto.getTimestamp())
                    .build();
        }
    }

    @SuperBuilder
    @Getter
    @NoArgsConstructor
    public static class RequestInfoAfterTime extends InfoAfterTime {

    }


    @Builder
    @Getter
    public static class SetDisconnectTime {
        private String roomId;
        private LocalDateTime disconnectAt;
        private Long userId;
    }

    @Builder
    @Getter
    public static class CreateRoom {
        private String name;
        private Long memberId;
        private Long tradeId;
    }


    @Builder
    @Getter
    public static class ManageMember {
        private String roomId;
        private Long memberId;
    }


    @Builder
    @Getter
    public static class ChangeRoomName {
        private String name;
        private String roomId;
    }

    @SuperBuilder
    @Getter
    @Setter
    public static class GetTradeInfo {
        private Long id;
        private String authorNickname;
        private String item;
        private Long price;
        private String tag;
        private Long count;

        private List<MemberInfo> memberList;

        public static GetTradeInfo of(PendingTrade pendingTrade) {
            return GetTradeInfo.builder()
                    .id(pendingTrade.getId())
                    .authorNickname(pendingTrade.getOwner().getNickname())
                    .item(pendingTrade.getItem())
                    .price(pendingTrade.getPrice())
                    .tag(pendingTrade.getTradeTag().getName())
                    .count(pendingTrade.getCount())
                    .build();
        }
    }
}