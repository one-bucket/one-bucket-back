package com.onebucket.domain.boardManage.dto.internal.post;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.internal.post
 * <br>file name      : MarketPostInfoDto
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
public class MarketPostInfoDto extends PostInfoDto {
    private String item;
    private Long joins;
    private Long wanted;
    private boolean isFin;
    private String location;

    private Long price;
    private Long count;
    private LocalDateTime dueDate;
}
