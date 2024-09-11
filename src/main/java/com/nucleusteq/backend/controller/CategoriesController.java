package com.nucleusteq.backend.controller;

import com.nucleusteq.backend.dto.BookDTO;
import com.nucleusteq.backend.dto.CategoryDTO;
import com.nucleusteq.backend.dto.ResponseDTO;
import com.nucleusteq.backend.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {

    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        List<CategoryDTO> categories = categoryService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping
    public Page<CategoryDTO> getAllCategories(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "7") int size,
                                     @RequestParam(required = false) String search) {
        return categoryService.getAllCategories(page, size, search);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable int id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> addCategory(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.addCategory(categoryDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateCategory(@PathVariable int id, @RequestBody CategoryDTO categoryDTO) {
        return categoryService.updateCategory(id, categoryDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteCategory(@PathVariable int id) {
        return categoryService.deleteCategory(id);
    }


}
