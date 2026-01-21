package com.agshin.extapp.services;

import com.agshin.extapp.exceptions.DataExistsException;
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

    public CategoryService(CategoryMapper mapper, CategoryRepository categoryRepository) {
        this.mapper = mapper;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public CategoryDto createCategory(String categoryName, User user) {
        categoryRepository.existsByCategoryName(categoryName)
                .orElseThrow(() -> new DataExistsException("Category with this name already exists"));

        Category category = mapper.toEntity(categoryName, user);

        categoryRepository.save(category);

        return mapper.toDto(category);
    }
}
