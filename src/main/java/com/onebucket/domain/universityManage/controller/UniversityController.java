package com.onebucket.domain.universityManage.controller;

import com.onebucket.domain.universityManage.dto.ResponseUniversityDto;
import com.onebucket.domain.universityManage.dto.RequestUniversityDto;
import com.onebucket.domain.universityManage.service.UniversityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.universityManage.controller
 * <br>file name      : UniversityController
 * <br>date           : 2024-07-05
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 * <pre>
 * modified log :
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-07-05        SeungHoon              init create
 * </pre>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/univ")
public class UniversityController {

    private final UniversityService universityService;

    @GetMapping
    public ResponseEntity<?> getAllUniversity() {
        List<ResponseUniversityDto> universities = universityService.findAll();
        return ResponseEntity.ok(universities);
    }

    @PostMapping
    public ResponseEntity<?> getUniversity(@Valid @RequestBody RequestUniversityDto readUniversityRequestDto) {
        ResponseUniversityDto requestDto = universityService.getUniversityByName(readUniversityRequestDto.getName());
        return ResponseEntity.ok(requestDto);
    }
}
