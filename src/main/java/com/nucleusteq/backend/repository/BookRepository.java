package com.nucleusteq.backend.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nucleusteq.backend.entity.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Books, Integer> {
    Page<Books> findAll(Pageable pageable);
}
