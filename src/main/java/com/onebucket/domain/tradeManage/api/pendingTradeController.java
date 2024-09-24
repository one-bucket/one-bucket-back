package com.onebucket.domain.tradeManage.api;

import com.onebucket.domain.tradeManage.service.PendingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.api
 * <br>file name      : pendingTradeController
 * <br>date           : 9/24/24
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
public class pendingTradeController {
    private final PendingServiceImpl pendingService;

    @GetMapping("/getTrade")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(pendingService.getAll());
    }
}
