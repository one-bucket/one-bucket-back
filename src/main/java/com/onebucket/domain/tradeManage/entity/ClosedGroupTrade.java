package com.onebucket.domain.tradeManage.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.entity
 * <br>file name      : ClosedGroupTrade
 * <br>date           : 2024-09-26
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
@DiscriminatorValue("closedGroupTrade")
public class ClosedGroupTrade extends GroupTrade {

}
