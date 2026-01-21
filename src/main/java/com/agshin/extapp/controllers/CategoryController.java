package com.agshin.extapp.controllers;

import com.agshin.extapp.config.CustomUserDetails;
import com.agshin.extapp.model.constants.ApplicationConstants;
import com.agshin.extapp.model.dto.category.CategoryDto;
import com.agshin.extapp.model.request.category.CreateCategoryRequest;
import com.agshin.extapp.model.response.GenericResponse;
import com.agshin.extapp.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<GenericResponse<CategoryDto>> createCategory(
            @RequestBody CreateCategoryRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        CategoryDto category = categoryService.createCategory(request.categoryName(), userDetails.getUser());

        var response = GenericResponse.create(
                ApplicationConstants.SUCCESS,
                category,
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }
}
