package com.onebucket.admin.categorize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * <br>package name   : com.onebucket.admin.categorize
 * <br>file name      : CategoryController
 * <br>date           : 11/4/24
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
@Controller
@RequestMapping("/dev/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // 메인 페이지 표시
    @GetMapping("/")
    public String showMainPage() {
        return "category_page/main";
    }

    // 카테고리 추가
    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestParam String name) {
        // 입력값에서 공백 및 특수문자 제거
        String cleanedName = name.replaceAll("[\\s\\p{Punct}]", "");

        if (cleanedName.isEmpty()) {
            // 입력값이 모두 제거되어 빈 문자열인 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 입력입니다.");
        }

        if (categoryRepository.findByName(cleanedName).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            Category category = new Category();
            category.setName(cleanedName);
            category.setCreatedAt(LocalDateTime.now());
            categoryRepository.save(category);
            return ResponseEntity.ok().build();
        }
    }

    // 카테고리 리스트 표시
    @GetMapping("/list")
    public String showCategories(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String search,
            Model model) {

        List<Category> categories;

        if (search != null && !search.isEmpty()) {
            categories = categoryRepository.findByNameContainingIgnoreCase(search);
        } else {
            categories = categoryRepository.findAll();
        }

        // 정렬 로직
        if ("name".equals(sort)) {
            if ("desc".equals(order)) {
                categories.sort(Comparator.comparing(Category::getName).reversed());
            } else {
                categories.sort(Comparator.comparing(Category::getName));
            }
        } else if ("createdAt".equals(sort)) {
            if ("desc".equals(order)) {
                categories.sort(Comparator.comparing(Category::getCreatedAt).reversed());
            } else {
                categories.sort(Comparator.comparing(Category::getCreatedAt));
            }
        }

        model.addAttribute("categories", categories);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        model.addAttribute("search", search);

        return "category_page/categories";
    }

    // 카테고리 삭제
    @PostMapping("/delete/id/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return "redirect:/dev/category/list";
    }

    @PostMapping("/delete/name/{name}")
    public ResponseEntity<?> deleteCategoryByName(@PathVariable String name) {
        // 입력값에서 공백 및 특수문자 제거
        String cleanedName = name.replaceAll("[\\s\\p{Punct}]", "");
        Optional<Category> category = categoryRepository.findByName(cleanedName);
        if (category.isPresent()) {
            categoryRepository.delete(category.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
