package com.onebucket.domain.tradeManage.service;

import com.onebucket.domain.tradeManage.dao.TradeTagRepository;
import com.onebucket.domain.tradeManage.entity.TradeTag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.service
 * <br>file name      : TradeTagServiceImpl
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

@Service
@RequiredArgsConstructor
public class TradeTagServiceImpl implements TradeTagService{

    private final TradeTagRepository tradeTagRepository;

    @Override
    public void addTag(String name) {
        TradeTag tag  = TradeTag.builder()
                .name(name)
                .build();
        tradeTagRepository.save(tag);
    }

    @Override
    public void deleteTag(String name) {
        tradeTagRepository.deleteById(name);
    }


}
