package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dto.postDto.GroupTradePostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.service
 * <br>file name      : MarketPostService
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
public interface MarketPostService extends BasePostService {
    Page<GroupTradePostDto.Thumbnail> getPostByTradeIdList(List<Long> tradeIds, Pageable pageable);
}
