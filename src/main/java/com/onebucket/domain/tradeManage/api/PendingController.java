package com.onebucket.domain.tradeManage.api;

import com.onebucket.domain.tradeManage.service.PendingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <br>package name   : com.onebucket.domain.boardManage.api
 * <br>file name      : PendingController
 * <br>date           : 2024-09-24
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
public class PendingController {
    private final PendingServiceImpl pendingService;

    @GetMapping("/getAllPend")
    public ResponseEntity<?> getAllPend() {
        return ResponseEntity.ok(pendingService.getAll());
    }
}
