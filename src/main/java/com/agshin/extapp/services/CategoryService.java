package com.agshin.extapp.services;

import com.agshin.extapp.exceptions.AuthorizationDeniedException;
import com.agshin.extapp.exceptions.DataExistsException;
import com.agshin.extapp.exceptions.DataNotFoundException;
import com.agshin.extapp.mappers.CategoryMapper;
import com.agshin.extapp.model.dto.category.CategoryDto;
import com.agshin.extapp.model.entities.Category;
import com.agshin.extapp.model.entities.User;
import com.agshin.extapp.model.response.category.CategoryResponse;
import com.agshin.extapp.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CategoryService {
    Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryMapper mapper;
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryMapper mapper, CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.mapper = mapper;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public CategoryDto createCategory(String categoryName, User user) {
        categoryRepository.existsByCategoryNameAndUser_Id(categoryName, user.getId())
                .orElseThrow(() -> {
                    logger.warn("Category not found for name: {}", categoryName);
                    return new DataExistsException("Category with this name already exists");
                });

        Category category = mapper.toEntity(categoryName, user);

        categoryRepository.save(category);
        logger.info("Successfully updated category ID: {} to Name: {}", category.getId(), categoryName);
        return mapper.toDto(category);
    }

    @Transactional
    public CategoryDto updateCategory(String categoryName, User user, Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Category with this name not found: " + categoryName));

        if (!Objects.equals(category.getUser().getId(), user.getId())) {
            logger.warn("User with ID: {} is trying to update category theyy do not own. Category ID: {}", user.getId(), id);
            throw new AuthorizationDeniedException("You don't own this category");
        }

        category.setCategoryName(categoryName);
        categoryRepository.save(category);

        return mapper.toDto(category);
    }

    public CategoryResponse getCategoriesForUser(User user, int page, int size, boolean asc) {
        Sort sort = asc
                ? Sort.by("id").ascending()
                : Sort.by("id").descending();

        PageRequest pageable = PageRequest.of(page, size, sort);

        Page<Category> byUserId = categoryRepository.findByUser_Id(user.getId(), pageable);

        List<CategoryDto> list = byUserId.getContent().stream()
                .map(mapper::toDto)
                .toList();

        return new CategoryResponse(list, byUserId.getNumber(), byUserId.getTotalElements(), byUserId.getTotalPages());
    }

    @Transactional
    public void deleteCategory(Long id, User user) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Category with this ID not found"));

        if (!Objects.equals(category.getUser().getId(), user.getId())) {
            logger.warn("User with ID: {} is trying to delete category they dont own. Category ID: {}", user.getId(), id);
            throw new AuthorizationDeniedException("You don't own this category");
        }

        categoryRepository.delete(category);
    }
}
