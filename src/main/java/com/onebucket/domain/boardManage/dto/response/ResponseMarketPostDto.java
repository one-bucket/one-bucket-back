package com.onebucket.domain.boardManage.dto.response;

import lombok.experimental.SuperBuilder;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.response
 * <br>file name      : ResponseMarketPostDto
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
@SuperBuilder
public class ResponseMarketPostDto extends ResponsePostDto {
    private String item;
    private int wanted;
    private String location;
}
