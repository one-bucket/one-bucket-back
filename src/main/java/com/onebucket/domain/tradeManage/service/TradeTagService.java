package com.onebucket.domain.tradeManage.service;


import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.service
 * <br>file name      : TradeTagService
 * <br>date           : 2024-10-06
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
public interface TradeTagService {

    void addTag(String name);

    void deleteTag(String name);
    List<String> getTagList();
}
