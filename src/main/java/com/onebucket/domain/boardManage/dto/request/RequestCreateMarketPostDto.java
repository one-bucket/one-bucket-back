package com.onebucket.domain.boardManage.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.request
 * <br>file name      : RequestCreateMarketPostDto
 * <br>date           : 2024-09-22
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
public class RequestCreateMarketPostDto extends RequestCreatePostDto {
    private String item;
    private int wanted;
    private String location;
}
