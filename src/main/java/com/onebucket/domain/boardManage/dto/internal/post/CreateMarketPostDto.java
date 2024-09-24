package com.onebucket.domain.boardManage.dto.internal.post;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.internal.post
 * <br>file name      : CreateMarketPostDto
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
public class CreateMarketPostDto extends CreatePostDto {
    private String item;
    private Long price;
    private Long count;

    private Long wanted;
    private String location;
    private LocalDateTime dueDate;
}
