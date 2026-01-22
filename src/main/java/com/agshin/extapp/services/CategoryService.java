package com.agshin.extapp.services;

import com.agshin.extapp.exceptions.DataExistsException;
import com.agshin.extapp.exceptions.DataNotFoundException;
import com.agshin.extapp.mappers.CategoryMapper;
import com.agshin.extapp.model.dto.category.CategoryDto;
import com.agshin.extapp.model.entities.Category;
import com.agshin.extapp.model.entities.User;
import com.agshin.extapp.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryMapper mapper;
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryMapper mapper, CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.mapper = mapper;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public CategoryDto createCategory(String categoryName, User user) {
        categoryRepository.existsByCategoryNameAndUser_Id(categoryName, user.getId())
                .orElseThrow(() -> new DataExistsException("Category with this name already exists"));

        Category category = mapper.toEntity(categoryName, user);

        categoryRepository.save(category);

        return mapper.toDto(category);
    }

    @Transactional
    public CategoryDto updateCategory(String categoryName, Long id) {
        Category category = categoryRepository.findByCategoryNameAndUser_Id(categoryName, id)
                .orElseThrow(() -> new DataNotFoundException("Category with this name not found: " + categoryName));

        category.setCategoryName(categoryName);
        categoryRepository.save(category);

        return mapper.toDto(category);
    }
}
