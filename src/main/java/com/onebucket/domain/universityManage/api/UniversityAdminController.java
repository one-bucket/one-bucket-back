package com.onebucket.domain.universityManage.api;

import com.onebucket.domain.universityManage.dto.university.DeleteUniversityDto;
import com.onebucket.domain.universityManage.dto.university.ResponseUniversityDto;
import com.onebucket.domain.universityManage.dto.university.UpdateUniversityDto;
import com.onebucket.domain.universityManage.service.UniversityService;
import com.onebucket.global.utils.SuccessResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.universityManage.api
 * <br>file name      : UniversityAdminController
 * <br>date           : 2024-09-23
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
 * 2024-09-23        SeungHoon              init create
 * </pre>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class UniversityAdminController {
    private final UniversityService universityService;

    @PostMapping("/univ")
    public ResponseEntity<SuccessResponseDto> createUniversity(@Valid @RequestBody ResponseUniversityDto responseUniversityDto) {
        Long id = universityService.createUniversity(responseUniversityDto);
        return ResponseEntity.ok(new SuccessResponseDto("success create university / id is " + id));
    }

    @GetMapping("/univs")
    public ResponseEntity<List<ResponseUniversityDto>> getAllUniversity() {
        List<ResponseUniversityDto> universities = universityService.findAllUniversity();
        return ResponseEntity.ok(universities);
    }

    @GetMapping("/univ/{name}")
    public ResponseEntity<ResponseUniversityDto> getUniversity(@PathVariable String name) {
        ResponseUniversityDto responseUniversityDto = universityService.getUniversity(name);
        return ResponseEntity.ok(responseUniversityDto);
    }

    @DeleteMapping("/univs")
    public ResponseEntity<SuccessResponseDto> deleteUniversity(@PathVariable DeleteUniversityDto dto) {
        universityService.deleteUniversity(dto);
        return ResponseEntity.ok(new SuccessResponseDto("success delete university"));
    }

    @PatchMapping("/univs")
    public ResponseEntity<SuccessResponseDto> updateUniversity(@Valid @RequestBody UpdateUniversityDto dto) {
        universityService.updateUniversity(dto);
        return ResponseEntity.ok(new SuccessResponseDto("success update university"));
    }
}
