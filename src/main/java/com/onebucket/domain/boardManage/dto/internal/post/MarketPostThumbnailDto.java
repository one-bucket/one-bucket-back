package com.onebucket.domain.boardManage.dto.internal.post;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

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
    private Long joins;
    private Long wanted;
    private boolean isFin;
    private Long price;
    private Long count;
    private LocalDateTime dueDate;
}
