package com.onebucket.domain.chatManager.entity;

import com.onebucket.domain.PushMessageManage.Entity.DeviceToken;
import com.onebucket.domain.memberManage.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.chatManager.entity
 * <br>file name      : ChatRoom
 * <br>date           : 2024-10-15
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
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ChatRoom {

    @Id
    private String id;

    private String name;

    private Long ownerId;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default // 기본값 설정
    private List<ChatRoomMember> members = new ArrayList<>();

    public void addMember(Member member) {
        ChatRoomMember chatRoomMember = ChatRoomMember.builder()
                .member(member)
                .chatRoom(this)
                .build();

        this.members.add(chatRoomMember);
    }

    @Enumerated(EnumType.STRING)
    private TradeType tradeType;

    private Long tradeId;


    @OneToMany
    @JoinTable(
            name = "chat_room_device_token",
            joinColumns = @JoinColumn(name = "chat_room_id"),
            inverseJoinColumns = @JoinColumn(name = "device_token_id")
    )
    @Builder.Default
    private List<DeviceToken> deviceTokens = new ArrayList<>();

    public void addDeviceToken(DeviceToken deviceToken) {
        this.deviceTokens.add(deviceToken);
    }

    public void removeDeviceToken(DeviceToken deviceToken) {
        this.deviceTokens.remove(deviceToken);
    }
}
