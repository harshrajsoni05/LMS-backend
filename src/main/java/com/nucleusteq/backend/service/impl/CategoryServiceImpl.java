package com.nucleusteq.backend.service.impl;

import com.nucleusteq.backend.dto.CategoryDTO;
import com.nucleusteq.backend.dto.ResponseDTO;
import com.nucleusteq.backend.entity.Category;
import com.nucleusteq.backend.exception.ResourceNotFoundException;
import com.nucleusteq.backend.exception.ResourceAlreadyExistsException;
import com.nucleusteq.backend.mapper.CategoryMapper;
import com.nucleusteq.backend.repository.CategoryRepository;
import com.nucleusteq.backend.repository.IssuanceRepository;
import com.nucleusteq.backend.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    private IssuanceRepository issuanceRepository;

    @Override
    public List<CategoryDTO> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryMapper::mapToCategoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CategoryDTO> getAllCategories(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Category> categories;

        if (search != null && !search.isEmpty()) {
            categories = categoryRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            categories = categoryRepository.findAll(pageable);
        }

        return categories.map(CategoryMapper::mapToCategoryDTO);
    }

    @Override
    public ResponseEntity<CategoryDTO> getCategoryById(int id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", String.valueOf(id)));

        return ResponseEntity.ok(CategoryMapper.mapToCategoryDTO(category));
    }

    @Override
    public ResponseEntity<ResponseDTO> addCategory(CategoryDTO categoryDTO) {
        boolean categoryExists = categoryRepository.existsByNameContainingIgnoreCase(categoryDTO.getName());
        if (categoryExists) {
            throw new ResourceAlreadyExistsException("Category with name '" + categoryDTO.getName() + "' already exists.");
        }

        Category category = CategoryMapper.mapToCategory(categoryDTO);
        Category savedCategory = categoryRepository.save(category);
        String message = "Category '" + category.getName() + "' added successfully";
        ResponseDTO response = new ResponseDTO("success", message);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ResponseDTO> updateCategory(int id, CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", String.valueOf(id)));

        existingCategory.setName(categoryDTO.getName());
        existingCategory.setDescription(categoryDTO.getDescription());
        Category updatedCategory = categoryRepository.save(existingCategory);

        String message = "Category '" + existingCategory.getName() + "' updated successfully";
        ResponseDTO response = new ResponseDTO("success", message);

        return ResponseEntity.ok(response);    }

    @Override
    public ResponseEntity<ResponseDTO> deleteCategory(int id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", String.valueOf(id)));


        categoryRepository.delete(category);

        String message = "Category '" + category.getName() + "' deleted successfully";
        ResponseDTO response = new ResponseDTO("success", message);

        return ResponseEntity.ok(response);     }
}
