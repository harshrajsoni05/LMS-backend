package com.nucleusteq.backend.mapper;

import com.nucleusteq.backend.dto.CategoryDTO;
import com.nucleusteq.backend.entity.Category;


public final class CategoryMapper {

    private CategoryMapper() {
        // Private constructor to prevent instantiation
    }

    public static CategoryDTO mapToCategoryDTO(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        return categoryDTO;
    }

    public static Category mapToCategory(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        return category;
    }
}



