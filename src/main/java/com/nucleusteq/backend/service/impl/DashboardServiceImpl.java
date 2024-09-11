package com.nucleusteq.backend.service.impl;

import com.nucleusteq.backend.dto.CountsDTO;
import com.nucleusteq.backend.entity.Books;
import com.nucleusteq.backend.repository.BookRepository;
import com.nucleusteq.backend.repository.CategoryRepository;
import com.nucleusteq.backend.repository.IssuanceRepository;
import com.nucleusteq.backend.repository.UserRepository;
import com.nucleusteq.backend.service.IDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardServiceImpl implements IDashboardService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private IssuanceRepository issuanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public CountsDTO getDashboardCounts() {
        // Retrieve all books
        List<Books> books = bookRepository.findAll();

        int totalBooks = books.stream()
                .mapToInt(Books::getQuantity)
                .sum();

        // Retrieve counts for categories, issuances, and users
        int categoriesCount = (int) categoryRepository.count();
        int issuancesCount = (int) issuanceRepository.count();
        int usersCount = (int) userRepository.count();

        // Create and return the CountsDTO with the calculated values
        return new CountsDTO(totalBooks, categoriesCount, issuancesCount, usersCount);
    }
}
