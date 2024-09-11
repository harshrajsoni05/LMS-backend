package com.nucleusteq.backend.repository;

import com.nucleusteq.backend.entity.Books;
import com.nucleusteq.backend.entity.Category;
import com.nucleusteq.backend.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    Page<Users> findAll(Pageable pageable);
    Optional<Users> findByEmail(String email);
    Optional<Users> findByNumber(String number);
    Page<Users> findByNameContainingIgnoreCase(String name,Pageable pageable);
    Page<Users> findByNameContainingIgnoreCaseAndRole(String name, String role, Pageable pageable);
    Page<Users> findByRole(String role, Pageable pageable);
    Page<Users> findByNumberContaining(String number, Pageable pageable);

}
