package com.onebucket.domain.tradeManage.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


/**
 * <br>package name   : com.onebucket.domain.tradeManage.entity
 * <br>file name      : pendingHistory
 * <br>date           : 9/24/24
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
@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue("GroupTrade")
@Setter
public class GroupTrade extends ClosedGroupTrade {

}
