package com.onebucket.domain.universityManage.api;

import com.onebucket.domain.universityManage.dto.*;
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
 * POST - "/admin/univ/create"
 * GET - "/admin/univs"
 * GET - "/admin/univ/{id}
 * DELETE - "/admin/univ/{id}
 * ---------------------------------------
 * PUT - "/admin/univ/{id}/email"
 * PUT - "/admin/univ/{id}/address"
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
@RequestMapping("/admin")
public class UniversityController {

    private final UniversityService universityService;

    @PostMapping("/univs")
    public ResponseEntity<?> createUniversity(@Valid @RequestBody CreateUniversityDto createUniversityDto) {
        Long id = universityService.createUniversity(createUniversityDto);
        return ResponseEntity.ok("create success, University id is " + id);
    }

    @GetMapping("/univs")
    public ResponseEntity<?> getAllUniversity() {
        List<ResponseUniversityDto> universities = universityService.findAllUniversity();
        return ResponseEntity.ok(universities);
    }

    @GetMapping("/univs/{id}")
    public ResponseEntity<?> getUniversity(@PathVariable Long id) {
        ResponseUniversityDto universityDto = universityService.getUniversity(id);
        return ResponseEntity.ok(universityDto);
    }

    @PatchMapping("/univs/{id}")
    public ResponseEntity<?> updateUniversityEmail(@PathVariable Long id, @Valid @RequestBody UpdateUniversityDto dto) {
        universityService.updateUniversity(id,dto);
        return ResponseEntity.ok("update email success");
    }

    @DeleteMapping("/univs/{id}")
    public ResponseEntity<?> deleteUniversity(@PathVariable Long id) {
        universityService.deleteUniversity(id);
        return ResponseEntity.ok("delete success");
    }
}
