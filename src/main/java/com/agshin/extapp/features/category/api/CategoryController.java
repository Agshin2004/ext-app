package com.agshin.extapp.features.category.api;

import com.agshin.extapp.features.category.api.dto.CategoryDto;
import com.agshin.extapp.features.category.api.dto.CreateCategoryRequest;
import com.agshin.extapp.features.category.api.dto.UpdateCategoryRequest;
import com.agshin.extapp.features.category.application.CategoryService;
import com.agshin.extapp.features.user.application.AuthService;
import com.agshin.extapp.shared.api.ApplicationConstants;
import com.agshin.extapp.shared.api.GenericResponse;
import com.agshin.extapp.shared.api.PagedResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final AuthService authService;

    public CategoryController(CategoryService categoryService, AuthService authService) {
        this.categoryService = categoryService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<GenericResponse<PagedResponse<List<CategoryDto>>>> getUserCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "true") boolean asc) {

        PagedResponse<List<CategoryDto>> categoriesForUser = categoryService.getCategoriesForUser(
                authService.getCurrentUser(),
                page,
                size,
                asc
        );

        var response = GenericResponse.create(ApplicationConstants.SUCCESS, categoriesForUser, HttpStatus.OK.value());

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(response);
    }

    @PostMapping
    public ResponseEntity<GenericResponse<CategoryDto>> createCategory(@RequestBody CreateCategoryRequest request) {
        CategoryDto category = categoryService.createCategory(request.categoryName(), authService.getCurrentUser());

        var response = GenericResponse.create(
                ApplicationConstants.SUCCESS,
                category,
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GenericResponse<CategoryDto>> updateCategory(
            @PathVariable Long id,
            @RequestBody UpdateCategoryRequest request) {
        CategoryDto categoryDto = categoryService.updateCategory(
                request.categoryName(),
                authService.getCurrentUser(),
                id
        );

        var response = GenericResponse.create(
                ApplicationConstants.SUCCESS,
                categoryDto,
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value())
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<Void>> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id, authService.getCurrentUser());

        GenericResponse<Void> response = GenericResponse.create(ApplicationConstants.SUCCESS, null, HttpStatus.NO_CONTENT.value());

        return ResponseEntity.status(HttpStatus.NO_CONTENT.value())
                .body(response);
    }
}
