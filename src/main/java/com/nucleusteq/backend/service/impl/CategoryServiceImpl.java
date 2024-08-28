package com.nucleusteq.backend.service;

import com.nucleusteq.backend.dto.CategoryDTO;
import com.nucleusteq.backend.entity.Category;
import com.nucleusteq.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl {

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<CategoryDTO> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(this::convertToDTO);  // Converts each Category entity to a CategoryDTO
    }

    public ResponseEntity<CategoryDTO> getCategoryById(int id) {
        return categoryRepository.findById(id)
                .map(category -> ResponseEntity.ok(convertToDTO(category)))
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<CategoryDTO> addCategory(CategoryDTO categoryDTO) {
        Category category = convertToEntity(categoryDTO);
        Category savedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(convertToDTO(savedCategory));
    }

    public ResponseEntity<CategoryDTO> updateCategory(int id, CategoryDTO categoryDTO) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(categoryDTO.getName());
                    existingCategory.setDescription(categoryDTO.getDescription());
                    categoryRepository.save(existingCategory);
                    return ResponseEntity.ok(convertToDTO(existingCategory));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> deleteCategory(int id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    categoryRepository.delete(category);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        return categoryDTO;
    }

    private Category convertToEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        return category;
    }
}
