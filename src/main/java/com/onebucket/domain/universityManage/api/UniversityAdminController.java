package com.onebucket.domain.universityManage.api;

import com.onebucket.domain.universityManage.dto.university.DeleteUniversityDto;
import com.onebucket.domain.universityManage.dto.university.GetUniversityDto;
import com.onebucket.domain.universityManage.dto.university.UniversityDto;
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
    public ResponseEntity<SuccessResponseDto> createUniversity(@Valid @RequestBody UniversityDto universityDto) {
        Long id = universityService.createUniversity(universityDto);
        return ResponseEntity.ok(new SuccessResponseDto("success create university / id is " + id));
    }

    @GetMapping("/univs")
    public ResponseEntity<List<UniversityDto>> getAllUniversity() {
        List<UniversityDto> universities = universityService.findAllUniversity();
        return ResponseEntity.ok(universities);
    }

    @GetMapping("/univ")
    public ResponseEntity<UniversityDto> getUniversity(@RequestBody GetUniversityDto dto) {
        UniversityDto universityDto = universityService.getUniversity(dto);
        return ResponseEntity.ok(universityDto);
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
