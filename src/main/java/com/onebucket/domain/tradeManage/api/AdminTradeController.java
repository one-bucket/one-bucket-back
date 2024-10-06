package com.onebucket.domain.tradeManage.api;

import com.onebucket.domain.tradeManage.dto.TagDto;
import com.onebucket.domain.tradeManage.service.TradeTagService;
import com.onebucket.global.utils.SuccessResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.api
 * <br>file name      : AdminTradeController
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
@RestController
@RequestMapping("/admin/trade")
@RequiredArgsConstructor
public class AdminTradeController {

    private final TradeTagService tradeTagService;

    @PostMapping("/tag/delete")
    public ResponseEntity<SuccessResponseDto> deleteTag(@Valid @RequestBody TagDto.TagName dto) {
        tradeTagService.deleteTag(dto.getName());
        return ResponseEntity.ok(new SuccessResponseDto("success delete tag : " + dto.getName()));
    }

    @PostMapping("/tag/create")
    public ResponseEntity<SuccessResponseDto> createTag(@Valid @RequestBody TagDto.TagName dto) {
        tradeTagService.addTag(dto.getName());
        return ResponseEntity.ok(new SuccessResponseDto("success create tag : " + dto.getName()));
    }
}
