package com.agshin.extapp.features.category.infrastructure;

import com.agshin.extapp.features.category.api.dto.CategoryDto;
import com.agshin.extapp.features.category.domain.Category;
import com.agshin.extapp.features.user.domain.User;
import com.agshin.extapp.features.user.infrastructure.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CategoryMapper {
    default Category toEntity(String categoryName, User user) {
        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setUser(user);
        return category;
    }

    @Mapping(target = "user", source = "user")
    CategoryDto toDto(Category category);
}
