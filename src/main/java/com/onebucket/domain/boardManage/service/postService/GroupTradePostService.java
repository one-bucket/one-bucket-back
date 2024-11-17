package com.onebucket.domain.boardManage.service.postService;

import com.onebucket.domain.boardManage.dto.postDto.GroupTradePostDto;
import org.springframework.data.domain.Page;

/**
 * <br>package name   : com.onebucket.domain.boardManage.service
 * <br>file name      : GroupTradePostService
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
public interface GroupTradePostService extends BasePostService {
    Page<GroupTradePostDto.InternalThumbnail> getPostByTradeIdList(GroupTradePostDto.TradeIdsPageDto dto);
}
