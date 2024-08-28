package com.nucleusteq.backend.repository;

import com.nucleusteq.backend.entity.Books;
import com.nucleusteq.backend.entity.Issuance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssuanceRepository extends JpaRepository<Issuance, Integer> {
    Page<Issuance> findAll(Pageable pageable);
}
