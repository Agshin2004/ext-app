package com.agshin.extapp.controllers;

import com.agshin.extapp.config.CustomUserDetails;
import com.agshin.extapp.model.constants.ApplicationConstants;
import com.agshin.extapp.model.dto.category.CategoryDto;
import com.agshin.extapp.model.entities.Category;
import com.agshin.extapp.model.request.category.CreateCategoryRequest;
import com.agshin.extapp.model.request.category.UpdateCategoryRequest;
import com.agshin.extapp.model.response.GenericResponse;
import com.agshin.extapp.model.response.category.CategoryResponse;
import com.agshin.extapp.services.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<GenericResponse<CategoryResponse>> getUserCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "true") boolean asc,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        CategoryResponse categoriesForUser = categoryService.getCategoriesForUser(
                userDetails.getUser(),
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
    public ResponseEntity<GenericResponse<CategoryDto>> createCategory(
            @RequestBody CreateCategoryRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        CategoryDto category = categoryService.createCategory(request.categoryName(), userDetails.getUser());

        var response = GenericResponse.create(
                ApplicationConstants.SUCCESS,
                category,
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GenericResponse<CategoryDto>> updateCategory(
            @PathVariable Long id,
            @RequestBody UpdateCategoryRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        CategoryDto categoryDto = categoryService.updateCategory(
                request.categoryName(),
                userDetails.getUser(),
                id
        );

        var response = GenericResponse.create(
                ApplicationConstants.SUCCESS,
                categoryDto,
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }


}
