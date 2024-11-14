package com.onebucket.domain.tradeManage.entity;

import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.entity
 * <br>file name      : ClosedUsedTrade
 * <br>date           : 11/13/24
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
@DiscriminatorValue("ClosedUsedTrade")
public class ClosedUsedTrade extends UsedTrade {
}
