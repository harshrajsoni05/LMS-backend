package com.nucleusteq.backend.service;

import com.nucleusteq.backend.dto.CategoryDTO;
import com.nucleusteq.backend.dto.ResponseDTO;
import com.nucleusteq.backend.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ICategoryService {

        List<CategoryDTO> getCategories();

        Page<CategoryDTO> getAllCategories(int page, int size, String search);

        ResponseEntity<CategoryDTO> getCategoryById(int id);

        ResponseEntity<ResponseDTO> addCategory(CategoryDTO categoryDTO);

        ResponseEntity<ResponseDTO> updateCategory(int id, CategoryDTO categoryDTO);

        ResponseEntity<ResponseDTO> deleteCategory(int id);


}
