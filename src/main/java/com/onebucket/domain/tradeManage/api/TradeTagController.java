package com.onebucket.domain.tradeManage.api;

import com.onebucket.domain.tradeManage.service.TradeTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.api
 * <br>file name      : TradeTagController
 * <br>date           : 11/15/24
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

@RestController
@RequiredArgsConstructor
public class TradeTagController {
    private final TradeTagService tradeTagService;

    @GetMapping("/trade/tag/list")
    public ResponseEntity<List<String>> getTagList() {
        List<String> tagList = tradeTagService.getTagList();
        return ResponseEntity.ok(tagList);
    }
}
