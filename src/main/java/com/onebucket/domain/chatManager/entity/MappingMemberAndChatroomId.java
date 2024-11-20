package com.onebucket.domain.chatManager.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * <br>package name   : com.onebucket.domain.chatManager.entity
 * <br>file name      : MappingMemberAndChatroomId
 * <br>date           : 2024-11-20
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
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MappingMemberAndChatroomId implements Serializable  {
    private Long trade;
    private Long member;

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        MappingMemberAndChatroomId mappingMemberAndChatroomId = (MappingMemberAndChatroomId) o;
        return Objects.equals(member, mappingMemberAndChatroomId.getMember())
               && Objects.equals(trade, mappingMemberAndChatroomId.getTrade());
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, trade);
    }
}
