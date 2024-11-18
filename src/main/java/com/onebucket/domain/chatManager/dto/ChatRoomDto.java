package com.onebucket.domain.chatManager.dto;

import com.onebucket.domain.chatManager.entity.TradeType;
import com.onebucket.domain.tradeManage.entity.GroupTrade;
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

    @SuperBuilder
    @Getter
    public static class CreateRoom {
        private String name;
        private Long ownerId;

        private TradeType tradeType;
        private Long tradeId;
    }

    @SuperBuilder
    @Getter
    public static class CreateAndJoinRoom extends CreateRoom{
        private Long joinerId;
    }

    @Builder
    @Getter
    public static class Info {
        private String roomId;
        private String name;
        private Long ownerId;

        private List<RoomMemberInfo> membersInfo;

        private TradeType tradeType;
        private Long tradeId;
        @Builder
        @Getter
        public static class RoomMemberInfo {
            private Long id;
            private String nickname;
            private LocalDateTime joinedAt;
            private String imageUrl;
        }
    }
    @Builder
    @Getter
    public static class MemberInfo {
        private Long id;
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

        private Long ownerId;
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

        public static GetTradeInfo of(GroupTrade groupTrade) {
            return GetTradeInfo.builder()
                    .id(groupTrade.getId())
                    .authorNickname(groupTrade.getOwner().getNickname())
                    .item(groupTrade.getItem())
                    .price(groupTrade.getPrice())
                    .tag(groupTrade.getTradeTag().getName())
                    .count(groupTrade.getCount())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class TradeIdentifier {
        private TradeType tradeType;
        private Long tradeId;
    }

    @Builder
    @Getter
    public static class SaveImage {
        private String name;
        private String format;
        private String roomId;
    }
}