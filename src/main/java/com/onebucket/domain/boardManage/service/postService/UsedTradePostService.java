package com.onebucket.domain.boardManage.service.postService;

import com.onebucket.domain.boardManage.dto.postDto.UsedTradePostDto;
import org.springframework.data.domain.Page;

/**
 * <br>package name   : com.onebucket.domain.boardManage.service.postService
 * <br>file name      : UsedTradeService
 * <br>date           : 2024-11-17
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
public interface UsedTradePostService {
    Page<UsedTradePostDto.InternalThumbnail> getPostByTradeIdList(UsedTradePostDto.TradeIdsPageDto dto);
}
