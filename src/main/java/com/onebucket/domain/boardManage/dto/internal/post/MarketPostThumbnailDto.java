package com.onebucket.domain.boardManage.dto.internal.post;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.internal
 * <br>file name      : MarketPostThumbnailDto
 * <br>date           : 2024-09-21
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
@SuperBuilder
public class MarketPostThumbnailDto extends PostThumbnailDto {
    private String item;
    private int joins;
    private int wanted;
    private boolean isFin;
}
