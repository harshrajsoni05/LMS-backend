package com.nucleusteq.backend.repository;
import com.nucleusteq.backend.dto.BookDTO;
import com.nucleusteq.backend.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nucleusteq.backend.entity.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Books, Integer> {
    Page<Books> findAll(Pageable pageable);
    List<Books> findAll();
    Page<Books> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author, Pageable pageable);
    List<Books> findByTitleContainingIgnoreCase(String query);
    boolean existsByTitle(String title);

}
