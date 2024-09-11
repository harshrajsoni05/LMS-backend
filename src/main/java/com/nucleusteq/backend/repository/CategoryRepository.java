package com.nucleusteq.backend.repository;

import com.nucleusteq.backend.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Page<Category> findAll(Pageable pageable);
    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Category> findAll();
    boolean existsByNameContainingIgnoreCase(String name);

}
