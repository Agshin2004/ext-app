package com.agshin.extapp.mappers;

import com.agshin.extapp.model.dto.category.CategoryDto;
import com.agshin.extapp.model.entities.Category;
import com.agshin.extapp.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "Spring",
        uses = {UserMapper.class}, // allows MapStruct to automatically map nested dtos/entities
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CategoryMapper {
    default Category toEntity(String categoryName, User user) {
        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setUser(user);
        return category;
    }

    @Mapping(target = "user", source = "user") // mapstruct will call UserMapper.toDto because signatures match User > UserDto
    CategoryDto toDto(Category category);
}
