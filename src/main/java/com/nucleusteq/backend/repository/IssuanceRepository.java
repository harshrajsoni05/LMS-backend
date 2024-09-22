package com.nucleusteq.backend.repository;

import com.nucleusteq.backend.dto.IssuanceOutDTO;
import com.nucleusteq.backend.entity.Books;
import com.nucleusteq.backend.entity.Issuance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IssuanceRepository extends JpaRepository<Issuance, Integer> {
    Page<Issuance> findAll(Pageable pageable);
    Page<Issuance> findByUserId(int userId, Pageable pageable);
    Page<Issuance> findByBookId(int BookId , Pageable pageable );

    boolean existsByBookCategoryIdAndStatus(int categoryId, String status);

    @Query("SELECT i FROM Issuance i " + "JOIN i.user u " + "JOIN i.book b " +
            "WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Issuance> findByUserNameOrBookTitle(@Param("search") String search, Pageable pageable);

    @Query("SELECT COUNT(i) FROM Issuance i WHERE i.status = 'Issued'")
    long countIssuedIssuances();

    @Query("SELECT i FROM Issuance i WHERE i.return_date BETWEEN :startOfTomorrow AND :endOfTomorrow AND i.status = :status")
    List<Issuance> findIssuancesByReturnDateBetweenAndStatus(
            @Param("startOfTomorrow") LocalDateTime startOfTomorrow,
            @Param("endOfTomorrow") LocalDateTime endOfTomorrow,
            @Param("status") String status
    );
    
}
