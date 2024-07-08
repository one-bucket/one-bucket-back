package com.onebucket.domain.universityManage.controller;

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
 * POST - "/admin/university/create"
 * GET - "/admin/university"
 * GET - "/admin/university/{id}
 * DELETE - "/admin/university/{id}
 * ---------------------------------------
 * 밑의 2개는 대학 정보를 추후 수정할 필요가 있는지 결정 후 사용. 현재는 구현만 하고 주석 처리 하였음.
 * PUT - "/admin/university/{id}/email"
 * PUT - "/admin/university/{id}/address"
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

    @PostMapping("/university/create")
    public ResponseEntity<?> createUniversity(@Valid @RequestBody CreateUniversityDto createUniversityDto) {
        Long id = universityService.createUniversity(createUniversityDto);
        return ResponseEntity.ok("create success, University id is " + id);
    }

    @GetMapping("/university")
    public ResponseEntity<?> getAllUniversity() {
        List<ResponseUniversityDto> universities = universityService.findAllUniversity();
        return ResponseEntity.ok(universities);
    }

    @GetMapping("/university/{id}")
    public ResponseEntity<?> getUniversity(@PathVariable Long id) {
        ResponseUniversityDto universityDto = universityService.getUniversity(id);
        return ResponseEntity.ok(universityDto);
    }

//    @PutMapping("/university/update/{id}/email")
//    public ResponseEntity<?> updateUniversityEmail(@PathVariable Long id, @Valid @RequestBody UpdateUniversityEmailDto updateUniversityEmailDto) {
//        universityService.updateUniversityEmail(id,updateUniversityEmailDto);
//        return ResponseEntity.ok("update email success");
//    }

//    @PutMapping("/university/update/{id}/address")
//    public ResponseEntity<?> updateUniversityAddress(@PathVariable Long id, @Valid @RequestBody UpdateUniversityAddressDto updateUniversityAddressDto) {
//        universityService.updateUniversityAddress(id,updateUniversityAddressDto);
//        return ResponseEntity.ok("update address success");
//    }

    @DeleteMapping("/university/delete/{id}")
    public ResponseEntity<?> deleteUniversity(@PathVariable Long id) {
        universityService.deleteUniversity(id);
        return ResponseEntity.ok("delete success");
    }
}
